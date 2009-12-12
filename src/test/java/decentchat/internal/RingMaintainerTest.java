package decentchat.internal;

import java.rmi.RemoteException;
import java.util.List;

import decentchat.internal.Hasher;
import decentchat.internal.NodeKey;
import decentchat.internal.RingMaintainer;
import decentchat.internal.remotes.Node;
import decentchat.internal.remotes.NodeImpl;

import org.junit.*;
import static org.junit.Assert.*;


public class RingMaintainerTest {
	
	private NodeImpl[] nodes;
	private RingMaintainer[] maintainers;
	
	private NodeImpl setupNode(int key, NodeImpl seed) throws RemoteException {
		return new NodeImpl(NodeKey.MIN_KEY.inc(key), seed);
	}
	
	private NodeImpl setupNode(int key) throws RemoteException {
		return new NodeImpl(NodeKey.MIN_KEY.inc(key));
	}
	
	@Before
	public void setup() throws RemoteException {
		int[] node_ids = {1, 8, 14, 21, 32, 38, 42, 48, 51, 56};
		nodes = new NodeImpl[node_ids.length];
		nodes[0] = setupNode(node_ids[0]);
		for (int i = 1; i < node_ids.length; ++i) {
			nodes[i] = setupNode(node_ids[i], nodes[0]);
		}
		maintainers = new RingMaintainer[nodes.length];
		for (int i = 0; i < nodes.length; ++i) {
			maintainers[i] = new RingMaintainer(nodes[i]);
		}
	}
	
	@Test
	public void testStabilize() {
		stabilizeNodes();		
		for (int i = 1; i < nodes.length; ++i) {
			assertEquals("Successor of " + nodes[i-1] + "is wrong", nodes[i], nodes[i-1].getSuccessor());
		}
	}

	private void stabilizeNodes() {
		for (int i = 0; i < 10; ++i) {
			for (int k = 0; k < nodes.length; ++k) {
				maintainers[k].stabilize();
			}
		}
	}
	
	@Test
	public void testFixFingers() throws RemoteException {
		stabilizeNodes();
		for (int i = 0; i < 6; ++i) {
			maintainers[1].fixFingers();
		}
		byte[] expected_fingers = {14,14,14,21,32,42};
		List<Node> fingers = nodes[1].getFingers();
		assertEquals(6, fingers.size());
		for (int i = 0; i < 6; ++i) {
			assertEquals(expected_fingers[i], fingers.get(i).getKey().getHash()[0]);
		}
		byte[] hash = Hasher.generateHash((byte)0);
		byte[] lookup = {8,9,63,52,56};
		byte[] expected_lookup = {8,14,1,56,56};
		for (int i = 0; i < lookup.length; ++i) {
			hash[0] = lookup[i];
			Node node = nodes[1].findSuccessor(new NodeKey(hash));
			assertEquals(expected_lookup[i], node.getKey().getHash()[0]);
		}
	}
	
	@Test
	public void testStabilizeFingersMix() throws RemoteException {
		RingMaintainer.MAX_FINGER_COUNT = 10;
		for (int i = 0; i < 100; ++i) {
			for (int k = 0; k < nodes.length; ++k) {
				maintainers[k].stabilize();
				maintainers[k].fixFingers();
			}
		}
	    int fingers[][] = {
	            {  8, 8, 8,14,21,38 },
	            { 14,14,14,21,32,42 },
	            { 21,21,21,32,32,48 },
	            { 32,32,32,32,38,56 },
	            { 38,38,38,42,48, 1 },
	            { 42,42,42,48,56, 1 },
	            { 48,48,48,51, 1, 1 },
	            { 51,51,56,56, 1, 1 },
	            { 56,56,56, 1, 1, 1 },
	            {  1, 1, 1, 1, 1, 1 }
	        };
	    for (int i = 0; i < fingers.length; ++i) {
	    	for (int k = 0; k < fingers[i].length; ++k) {
	    		assertEquals("Finger #" + (k+1) + " of  node #" + (i+1) + " is wrong",
	    				fingers[i][k], nodes[i].getFingers().get(k).getKey().getHash()[0]);
	    	}
	    }
	}
	
	@Test
	public void testFixSuccessors() throws RemoteException {
		for (int i = 0; i < 10; ++i) {
			for (int k = 0; k < nodes.length; ++k) {
				maintainers[k].stabilize();
				maintainers[k].fixSuccessors();
			}
		}
		int successors[][] = {
			    {  8, 14, 21 },
		        { 14, 21, 32 },
		        { 21, 32, 38 },
		        { 32, 38, 42 },
		        { 38, 42, 48 },
		        { 42, 48, 51 },
		        { 48, 51, 56 },
		        { 51, 56,  1 },
		        { 56,  1,  8 },
		        {  1,  8, 14 },	
		};
		for (int i = 0; i < successors.length; ++i) {
			for (int k = 0; k < successors[i].length; ++k) {
				assertEquals("Successor #" + (k+1) + " of node #" + (i+1) + " is wrong",
						successors[i][k], nodes[i].getSuccessors().get(k).getKey().getHash()[0]);
			}
		}
	}

}

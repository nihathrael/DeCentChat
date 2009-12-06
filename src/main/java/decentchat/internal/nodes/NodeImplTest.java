package decentchat.internal.nodes;

import java.rmi.RemoteException;

import org.junit.*;

import decentchat.internal.NodeKey;
import static org.junit.Assert.*;

public class NodeImplTest {
	
	private NodeImpl seed_node;
	private NodeImpl[] nodes;
	
	@Before
	public void setup() throws RemoteException {
		seed_node = new NodeImpl(NodeKey.MIN_KEY);
	}
	
	private void setupNodes() throws RemoteException {
		int[] node_ids = {1, 8, 14, 21, 32, 38, 42, 48, 51, 56};
		nodes = new NodeImpl[node_ids.length];
		nodes[0] = setupNode(node_ids[0]);
		for (int i = 1; i < node_ids.length; ++i) {
			nodes[i] = setupNode(node_ids[i], nodes[0]);
		}
	}
	
	private NodeImpl setupNode(int key, NodeImpl seed) throws RemoteException {
		return new NodeImpl(NodeKey.MIN_KEY.inc(key), seed);
	}
	
	private NodeImpl setupNode(int key) throws RemoteException {
		return new NodeImpl(NodeKey.MIN_KEY.inc(key));
	}
	
	@Test
	public void testCreate() {
		assertNull(seed_node.getPredecessor());
		assertEquals(seed_node, seed_node.getSuccessor());
		assertEquals(0, seed_node.getSuccessors().size());
		assertEquals(0, seed_node.getFingers().size());
	}
	
	@Test
	public void testJoin() throws RemoteException {
		NodeImpl eight = setupNode(8);
		NodeImpl ten = setupNode(10, eight);
		assertNull(ten.getPredecessor());
		assertEquals(eight, ten.getSuccessor());
	}
	
	@Test
	public void testJoinBackwards() throws RemoteException {
		NodeImpl ten = setupNode(10);
		NodeImpl eight = setupNode(8, ten);
		assertNull(eight.getPredecessor());
		assertEquals(ten, eight.getSuccessor());
	}

}

package decentchat.internal.nodes;

import java.rmi.RemoteException;

import org.junit.*;

import decentchat.internal.NodeKey;
import decentchat.internal.remotes.NodeImpl;
import static org.junit.Assert.*;

public class NodeImplTest {
	
	private NodeImpl setupNode(int key, NodeImpl seed) throws RemoteException {
		return new NodeImpl(NodeKey.MIN_KEY.inc(key), seed);
	}
	
	private NodeImpl setupNode(int key) throws RemoteException {
		return new NodeImpl(NodeKey.MIN_KEY.inc(key));
	}
	
	@Test
	public void testCreate() throws RemoteException {
		NodeImpl seed_node = setupNode(0);
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
	
	@Test
	public void testNotify() throws RemoteException {
		NodeImpl ten = setupNode(10);
		NodeImpl eight = setupNode(8, ten);
		eight.notify(ten);
		assertEquals(ten, eight.getPredecessor());
		ten.notify(eight);
		assertEquals(eight, ten.getPredecessor());
	}

}

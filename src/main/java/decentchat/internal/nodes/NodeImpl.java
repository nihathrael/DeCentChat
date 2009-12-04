package decentchat.internal.nodes;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NodeImpl extends UnicastRemoteObject implements Node {
	
	private NodeKey key;
	private int port;
	private Node predecessor;
	private List<Node> successors;

	public NodeImpl() throws RemoteException {
		super();
	}

	@Override
	public Pair<Node, Boolean> getCloserNode(NodeKey wanted) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeKey getKey() {
		return key;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public Node getPredecessor() {
		return predecessor;
	}

	@Override
	public List<Node> getSuccessors() {
		return successors;
	}

	@Override
	public void notify(Node predecessor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIp() {
		try {
			return NodeImpl.getClientHost();
		} catch (ServerNotActiveException e) {
			return null;
		}
	}

}

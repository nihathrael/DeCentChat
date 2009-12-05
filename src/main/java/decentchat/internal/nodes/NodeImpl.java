package decentchat.internal.nodes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import decentchat.internal.NodeKey;
import decentchat.internal.Pair;

public class NodeImpl extends UnicastRemoteObject implements Node, Remote {
	
	private NodeKey key;
	private int port;
	private Node predecessor;
	private Node successor;
	private List<Node> successors;
	private List<Node> fingers;

	protected NodeImpl() throws RemoteException {
		super();
	}

	@Override
	public Pair<Node, Boolean> getCloserNode(NodeKey wanted) {
	    wanted = wanted.inc(1);
	    if (wanted == key) {
	        return new Pair<Node, Boolean>(this, true);
	    } else if (wanted.isWithin(key, successor.getKey())) {
	        return new Pair<Node, Boolean>(successor, true);
	    } else {
	        return new Pair<Node, Boolean>(closestPrecedingNode(wanted), false);
	    }
	}

	private Node closestPrecedingNode(NodeKey wanted) {
	    List<Node> nlist = buildNodeList();
	    for (int i = 0; i < nlist.size(); ++i) {
	         if (nlist.get(i).getKey().isWithin(key, wanted)) {
	            return nlist.get(i);
	         }
	    }
	    return successor;
	}

	private List<Node> buildNodeList() {
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
	public void notify(Node n) {
	    if (predecessor == null || n.getKey().isWithin(predecessor.getKey(), key)) {
	        predecessor = n;
	    }
	}

	@Override
	public Node findSuccessor(NodeKey wanted) {
	    Node n = this;
	    while (true) {
	        Pair<Node, Boolean> pair = n.getCloserNode(wanted);
	        n = pair.first;
	        if (pair.second) return n;
	    }
	}

}

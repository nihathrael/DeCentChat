package decentchat.internal.nodes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import decentchat.internal.NodeKey;
import decentchat.internal.Pair;

public class NodeImpl extends UnicastRemoteObject implements Node, Remote {
	
	private NodeKey key;
	private Node predecessor = null;
	private Node successor = null;
	private List<Node> successors = new LinkedList<Node>();
	private List<Node> fingers = new LinkedList<Node>();
	private int last_fixed_finger = -1;

	public NodeImpl(NodeKey key, Node join) throws RemoteException {
		super();
		this.key = key;
	    successor = findSuccessor(key, join);
	    successor.notify(this);
	}

	public NodeImpl(NodeKey key) throws RemoteException {
		super();
		this.key = key;
	    successor = this;
	}

	@Override
	public Node findSuccessor(NodeKey wanted) {
		return findSuccessor(wanted, this);
	}
	
	public Node findSuccessor(NodeKey wanted, Node start) {
	    Node n = start;
	    while (true) {
	        Pair<Node, Boolean> pair = n.getCloserNode(wanted);
	        n = pair.first;
	        if (pair.second) return n;
	    }
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

	/**
	 * Builds a list of all known nodes, ordered
	 * by their proximity to this node when going
	 * up the chord ring.
	 * @return A list of all known nodes.
	 */
	private List<Node> buildNodeList() {
	    if (successors.size() == 0) {
	        return fingers;
	    }
	    List<Node> node_list = new LinkedList<Node>();
	    Node last_successor = successors.get(successors.size()-1);
	    for (int i = 0; i < fingers.size(); ++i) {
	        if (fingers.get(i).getKey().isWithin(key, last_successor.getKey())) {
	            break;
	        } else {
	            node_list.add(fingers.get(i));
	        }
	    }
	    node_list.addAll(successors);
	    return node_list;
	}
	
	public void stabilize() {
		NodeKey hash = key.inc(1);
	    Node x = successor.getPredecessor();
	    if (x != null &&
	    		// we're either pointing to ourselves
	    		((successor.getKey().compareTo(key) == 0 &&x.getKey().compareTo(key) != 0)
	    		// or there is a node between us and successor
	            || (x.getKey().isWithin(hash, successor.getKey())))) {
	        successor = x;
	        successors.add(0, x);
	    }
	    successor.notify(this);
	}
	
	public void fixFingers() {
		last_fixed_finger += 1;
	    if (last_fixed_finger >= fingers.size()) {
	        last_fixed_finger = 0;
	    }
	    NodeKey hash = key.inc((long) Math.pow(2, last_fixed_finger));
	    fingers.set(last_fixed_finger, findSuccessor(hash));
	}
	
	public void fixSuccessors() {
	    List<Node> succs = successor.getSuccessors();
	    succs.add(0, successor);
	    if (succs.size() > successors.size()) {
	    	succs = succs.subList(0, successors.size()-1);
	    }
	    successors = succs;
	}

	@Override
	public NodeKey getKey() {
		return key;
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
	public String getIP() {
		try {
			return NodeImpl.getClientHost();
		} catch (ServerNotActiveException e) {
			return null;
		}
	}

}

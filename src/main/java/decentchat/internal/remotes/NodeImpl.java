package decentchat.internal.remotes;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import decentchat.internal.NodeKey;
import decentchat.util.Pair;

public class NodeImpl extends UnicastRemoteObject implements Node {

	private static final long serialVersionUID = 8533464683952827752L;

	static Logger logger = Logger.getLogger(NodeImpl.class);

	private NodeKey key;
	private Node predecessor = null;
	private Node successor = null;
	private List<Node> successors = new LinkedList<Node>();
	private List<Node> fingers = new LinkedList<Node>();

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
	public Node findSuccessor(NodeKey wanted) throws RemoteException {
		return findSuccessor(wanted, this);
	}

	public Node findSuccessor(NodeKey wanted, Node start)
			throws RemoteException {
		Node n = start;
		while (true) {
			Pair<Node, Boolean> pair = n.findCloserNode(wanted);
			n = pair.first;
			if (pair.second)
				return n;
		}
	}

	@Override
	public Pair<Node, Boolean> findCloserNode(NodeKey wanted)
			throws RemoteException {
		if (wanted == key.inc(1)) {
			return new Pair<Node, Boolean>(this, true);
		} else if (wanted.isWithin(key.inc(1), successor.getKey())) {
			return new Pair<Node, Boolean>(successor, true);
		} else {
			return new Pair<Node, Boolean>(closestPrecedingNode(wanted), false);
		}
	}

	private Node closestPrecedingNode(NodeKey wanted) {
		List<Node> nlist = buildNodeList();
		for (int i = 0; i < nlist.size(); ++i) {
			try {
				if (nlist.get(i).getKey().isWithin(key, wanted)) {
					return nlist.get(i);
				}
			} catch (RemoteException e) {
				// continue with next node
			}
		}
		return successor;
	}

	/**
	 * Builds a list of all known nodes, ordered by their proximity to this node
	 * when going up the chord ring.
	 * 
	 * @return A list of all known nodes.
	 * @throws RemoteException
	 */
	private List<Node> buildNodeList() {
		if (successors.size() == 0) {
			return fingers;
		}
		List<Node> node_list = new LinkedList<Node>();
		Node last_successor = successors.get(successors.size() - 1);
		for (int i = 0; i < fingers.size(); ++i) {
			try {
				if (fingers.get(i).getKey().isWithin(key,
						last_successor.getKey())) {
					break;
				} else {
					node_list.add(fingers.get(i));
				}
			} catch (RemoteException e) {
				// continue
			}
		}
		node_list.addAll(successors);
		return node_list;
	}

	@Override
	public void notify(Node n) throws RemoteException {
		logger.debug("Got notified by " + n);
		if (predecessor == null
				|| n.getKey().isWithin(predecessor.getKey(), key)) {
			predecessor = n;
		}
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

	public boolean removeSuccessorFromList(Node succ) {
		return successors.remove(succ);
	}

	@Override
	public String getIP() {
		try {
			logger.debug("Returning ip:" + NodeImpl.getClientHost());
			return NodeImpl.getClientHost();
		} catch (ServerNotActiveException e) {
			logger.debug("Error retrieving ip");
			return null;
		}
	}

	public Node getSuccessor() {
		return successor;
	}

	public void setSuccessor(Node successor) {
		this.successor = successor;
	}

	public List<Node> getFingers() {
		return fingers;
	}

	public String toString() {
		return key.toString();
	}

	public void setSuccessors(List<Node> successors) {
		this.successors = successors;
	}

}

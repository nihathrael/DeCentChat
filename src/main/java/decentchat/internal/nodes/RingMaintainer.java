package decentchat.internal.nodes;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import decentchat.internal.NodeKey;

public class RingMaintainer extends Thread {
	
	private static final int SLEEP_TIME = 1000;

	static Logger logger = Logger.getLogger(RingMaintainer.class);
	
	private NodeImpl node;
	private int last_fixed_finger;
	private boolean stillRunning;
	
	public void abort() {
		stillRunning = false;
	}
	
	public RingMaintainer(NodeImpl node) {
		this.node = node;
		last_fixed_finger = -1;
		stillRunning = true;
	}

	public void stabilize() {
		NodeKey hash = node.getKey().inc(1);
	    Node x;
		try {
			x = node.getSuccessor().getPredecessor();
		    if (x != null &&
		    		// we're either pointing to ourselves
		    		((node.getSuccessor().getKey().compareTo(node.getKey()) == 0 &&x.getKey().compareTo(node.getKey()) != 0)
		    		// or there is a node between us and node.getSuccessor()
		            || (x.getKey().isWithin(hash, node.getSuccessor().getKey())))) {
		        node.setSuccessor(x);
		        logger.debug("New successor is " + x);
		        node.getSuccessors().add(0, x);
		    }
		    node.getSuccessor().notify(node);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fixFingers() {
	    try {
			last_fixed_finger += 1;
		    if (last_fixed_finger >= NodeImpl.MAX_FINGER_COUNT) {
		        last_fixed_finger = 0;
		    }
		    NodeKey hash = node.getKey().inc((long) Math.pow(2, last_fixed_finger));
		    if (node.getFingers().size() <= last_fixed_finger) {
		    	node.getFingers().add(node.findSuccessor(hash));
		    } else {
		    	node.getFingers().set(last_fixed_finger, node.findSuccessor(hash));
		    }
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fixSuccessors() {
	    List<Node> succs;
		try {
			succs = node.getSuccessor().getSuccessors();
		    succs.add(0, node.getSuccessor());
		    if (succs.size() > NodeImpl.MAX_SUCCESSOR_COUNT) {
		    	succs = succs.subList(0, NodeImpl.MAX_SUCCESSOR_COUNT-1);
		    }
		    node.getSuccessors().clear();
		    node.getSuccessors().addAll(succs);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		logger.debug("maintenance loop started");
		while (true) {
			stabilize();
			if (!stillRunning) return;
			fixSuccessors();
			if (!stillRunning) return;
			fixFingers();
			if (!stillRunning) return;
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
			}
			if (!stillRunning) return;
		}
	}
	
}

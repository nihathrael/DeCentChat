package decentchat.internal.remotes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import decentchat.internal.NodeKey;
import decentchat.util.Pair;

public interface Node extends Remote {
	
	NodeKey getKey() throws RemoteException;
	String getIP() throws RemoteException;
	Node getPredecessor() throws RemoteException;
	List<Node> getSuccessors() throws RemoteException;
	
	void notify(Node predecessor) throws RemoteException;
	Pair<Node, Boolean> findCloserNode(NodeKey wanted) throws RemoteException;
	Node findSuccessor(NodeKey wanted) throws RemoteException;

}

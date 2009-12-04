package decentchat.internal.nodes;

import java.util.List;

public interface Node {
	
	NodeKey getKey();
	Node getPredecessor();
	List<Node> getSuccessors();
	void notify(Node predecessor);
	Pair<Node, Boolean> getCloserNode(NodeKey wanted);
	int getPort();

}

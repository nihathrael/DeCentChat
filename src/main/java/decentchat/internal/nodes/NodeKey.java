package decentchat.internal.nodes;

import java.util.Arrays;

public class NodeKey {
	
	private char[] hash;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(hash);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeKey other = (NodeKey) obj;
		if (!Arrays.equals(hash, other.hash))
			return false;
		return true;
	}

	public NodeKey inc() {
		// TODO Auto-generated method stub
		return null;
	}

}

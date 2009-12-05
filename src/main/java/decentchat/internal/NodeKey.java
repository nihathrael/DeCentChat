package decentchat.internal;

import java.util.Arrays;

import decentchat.internal.nodes.Node;

public class NodeKey {
	
	private byte[] hash;
	
	public NodeKey(byte[] hash) {
		this.hash = hash;
	}

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

	public NodeKey inc(int offset) {
		byte[] next = Arrays.copyOf(hash, hash.length);
	    byte carry = 0;
	    byte ptr = 0;
	    while (offset > 0 || carry > 0) {
	        if (ptr == Hasher.HASH_LENGTH_IN_BYTES)
	            break;
	        short val = (short) (next[ptr] + (offset & 0xFF) + carry);
	        if (val > 255) {
	            carry = 1;
	            next[ptr] = (byte) (val & 0xFF);
	        } else {
	            carry = 0;
	            next[ptr] = (byte) val;
	        }
	        offset = offset >> 8;
	        ++ptr;
	    }
		return new NodeKey(next);
	}

	public boolean isWithin(NodeKey first, NodeKey last) {
		// TODO Auto-generated method stub
		return false;
	}

}

package decentchat.internal;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;

public class NodeKey implements Comparable<NodeKey>, Serializable {

	private static final long serialVersionUID = -8633504133195223816L;
	
	public static final NodeKey MIN_KEY = new NodeKey(Hasher.generateHash(Byte.MIN_VALUE));
	public static final NodeKey MAX_KEY = new NodeKey(Hasher.generateHash(Byte.MAX_VALUE));
	
	private byte[] hash;
	
	public NodeKey(byte[] hash) {
		this.hash = hash;
	}

	/**
	 * Returns a new NodeKey whose hash is bigger
	 * than the current one.
	 * @param amount How much to add to the current key.
	 * @return The NodeKey that is bigger than the current one.
	 */
	public NodeKey inc(long amount) {
		byte[] next = Arrays.copyOf(hash, hash.length);
	    byte carry = 0;
	    byte ptr = 0;
	    while (amount > 0 || carry > 0) {
	        if (ptr == Hasher.HASH_LENGTH_IN_BYTES)
	            break;
	        short val = (short) (next[ptr] + (amount & 0xFF) + carry);
	        if (val > 255) {
	            carry = 1;
	            next[ptr] = (byte) (val & 0xFF);
	        } else {
	            carry = 0;
	            next[ptr] = (byte) val;
	        }
	        amount = amount >> 8;
	        ++ptr;
	    }
		return new NodeKey(next);
	}

	/**
	 * Returns true if this key lies between (and including)
	 * the two given nodes.
	 * @param first The first node in the range.
	 * @param last The last node in the range.
	 */
	public boolean isWithin(NodeKey first, NodeKey last) {
		if (first.compareTo(last) > 0) {
			return (first.compareTo(this) <= 0 && this.compareTo(MAX_KEY) <= 0)
					|| (MIN_KEY.compareTo(this) <= 0 && this.compareTo(last) <= 0);
		} else {
			return first.compareTo(this) <= 0 && this.compareTo(last) <= 0;
		}
	}

	@Override
	public int compareTo(NodeKey o) {
	    for (int i = hash.length-1; i >= 0; --i) {
	        if (this.hash[i] > o.hash[i]) return 1;
	        else if (this.hash[i] < o.hash[i]) return -1;
	    }
	    return 0;
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
	
	public String toString() {
	    return new BigInteger(hash).toString(16);
	}

}

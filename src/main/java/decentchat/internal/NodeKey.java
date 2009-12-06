package decentchat.internal;

import java.io.Serializable;
import java.util.Arrays;

public class NodeKey implements Comparable<NodeKey>, Serializable {

	private static final long serialVersionUID = -8633504133195223816L;

	public static final NodeKey MIN_KEY = new NodeKey(Hasher.MIN_HASH);
	public static final NodeKey MAX_KEY = new NodeKey(Hasher.MAX_HASH);

	private byte[] hash;

	public NodeKey(byte[] hash) {
		this.hash = hash;
	}

	/**
	 * Returns a new NodeKey whose hash is bigger than the current one.
	 * @param amount How much to add to the current key. Must be positive.
	 * @return The NodeKey that is bigger than the current one.
	 */
	public NodeKey inc(long amount) {
		byte[] inced_key = Arrays.copyOf(hash, hash.length);
		int[] normalized_hash = normalize(hash);
		byte carry = 0;
		int ptr = 0;
		while (amount > 0 || carry > 0) {
			if (ptr == Hasher.HASH_LENGTH_IN_BYTES)
				break;
			int val = (int) (normalized_hash[ptr] + (amount & 0xFF) + carry);
			if (val > 255) {
				carry = 1;
				val &= 0xFF;
			} else {
				carry = 0;
			}
			if (val > 127) {
				val -= 256;
			}
			inced_key[ptr] = (byte) (val);
			amount = amount >> 8;
			++ptr;
		}
		return new NodeKey(inced_key);
	}
	
	/**
	 * Returns a normalized version of the given hash,
	 * i.e. the values [0, ..., 127, -128, ..., -1]
	 * will be transformed to [0, ..., 255].
	 * @param hash The hash to normalize.
	 * @return The normalized hash.
	 */
	private int[] normalize(byte[] hash) {
		int[] normalized_hash = new int[hash.length];
		for (int i = 0; i < hash.length; ++i) {
			if (hash[i] < 0) {
				normalized_hash[i] = hash[i] + 256;
			} else {
				normalized_hash[i] = hash[i];
			}
		}
		return normalized_hash;
	}

	/**
	 * Returns true if this key lies between (and including) the two given nodes.
	 * @param first The first node in the range.
	 * @param last The last node in the range.
	 * @return Whether or not this node is in the given Range.
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
		int[] this_hash = normalize(hash);
		int[] o_hash = normalize(o.hash);
		for (int i = hash.length - 1; i >= 0; --i) {
			if (this_hash[i] > o_hash[i])
				return 1;
			else if (this_hash[i] < o_hash[i])
				return -1;
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
		String result = "";
		for (int i = 0; i < hash.length; i++) {
			result += Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

}

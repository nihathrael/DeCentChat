package decentchat.internal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;

public class Hasher {
	
	public final static int HASH_LENGTH_IN_BYTES = 40;
	public final static String HASH_ALGORITHM = "SHA-1";

	public final static byte[] MIN_HASH = generateHash((byte)0);
	public final static byte[] MAX_HASH = generateHash((byte)-1);

	/**
	 * Hashes the given input string using the hash
	 * algorithm specified by HASH_LENGTH_IN_BYTES.
	 * @param input
	 * @return
	 */
	public static byte[] generateHash(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    md.update(input.getBytes(), 0, input.length());
	    return md.digest();
	}

	/**
	 * Generates a hash filled with the given byte.
	 * @param value The byte to fill the hash with.
	 * @return The generated hash.
	 */
	public static byte[] generateHash(byte value) {
		byte[] hash = new byte[HASH_LENGTH_IN_BYTES];
		Arrays.fill(hash, value);
		return hash;
	}

	public static byte[] fromPublicKey(PublicKey pubkey) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

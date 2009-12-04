package decentchat.internal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	
	public final static int HASH_LENGTH_IN_BYTES = 40;
	public final static String HASH_ALGORITHM = "SHA-1";

	public static byte[] generateHash(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] sha1hash = new byte[40];
	    md.update(input.getBytes(), 0, input.length());
	    return md.digest();
	}
	
}

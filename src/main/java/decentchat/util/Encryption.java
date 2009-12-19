package decentchat.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;


public class Encryption {

	private static final String ENCRYPTION_ALGORITHM = "RSA";
	
	public static Cipher getCipher() {
		try {
			return Cipher.getInstance(Encryption.ENCRYPTION_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

package decentchat.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;


public class Encryption {

	private static final String ENCRYPTION_ALGORITHM = "RSA";
	
	private static Logger logger = Logger.getLogger(Encryption.class);
	
	public static Cipher getCipher() {
		try {
			return Cipher.getInstance(Encryption.ENCRYPTION_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			logger.fatal("Unsupported encryption algorithm: " + ENCRYPTION_ALGORITHM, e);
			throw new RuntimeException("Unsupported encryption algorithm: " + ENCRYPTION_ALGORITHM, e);
		} catch (NoSuchPaddingException e) {
			logger.fatal("Unsupported encryption algorithm: " + ENCRYPTION_ALGORITHM, e);
			throw new RuntimeException("Unsupported encryption algorithm: " + ENCRYPTION_ALGORITHM, e);
		}
	}

}

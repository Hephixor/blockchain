package merkle;

import java.io.UnsupportedEncodingException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hash {

	private static String algorithm = "HmacSHA256";
	private static String key = "256bitsKey-ABCDEFGHIJKLMNOPQRSTU";

	/**
	 * Hash un tableau de bytes avec Hmac SHA-256.
	 * @param data tableau de bytes à hasher.
	 * @return hash sur 256 bits à stocker dans un noeud de Merkle Tree.
	 */
	public static byte[] digest(byte[] data) {
		//byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
		
	    Mac hasher = null;
	    
		try {
			hasher = Mac.getInstance(algorithm);
			hasher.init(new SecretKeySpec(Bytes.fromStr(key), algorithm));
		} catch (Exception e) {
			e.printStackTrace();
		}
    
	    byte[] hash = hasher.doFinal(data);
		
		return hash;
	}

}

package merkle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hash {

	/**
	 * Hash un tableau de bytes avec SHA-256.
	 * @param data tableau de bytes à hasher.
	 * @return hash sur 256 bits à stocker dans un noeud de Merkle Tree.
	 */
	public static byte[] digestSHA256(byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return md.digest(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Hash un tableau de bytes avec Hmac SHA-256.
	 * @param data tableau de bytes à hasher.
	 * @return hash sur 256 bits à stocker dans un noeud de Merkle Tree.
	 */
	public static byte[] digestHMAC(byte[] data, String key) {
	    Mac hasher = null;

		try {
			hasher = Mac.getInstance("HmacSHA256");
			hasher.init(new SecretKeySpec(Convert.stringToBytes(key), "HmacSHA256"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    
	    byte[] hash = hasher.doFinal(data);
		
		return hash;
	}

	public static byte[] digest(byte[] data) {
	    return data;
    }


    /**
     * Hash une chaîne de charactères en utilisant l'algorithme SHA-256.
     * @param data chaîne de charactères à hasher.
     * @return tableau d'octets représentant un hash sur 256 bits.
     */
	public static byte[] digestSHA256String(String data) {
	    return digestSHA256(Convert.stringToBytes(data));
    }
	
	public static byte[] digestHMACString(String data, String key) {
	    return digestHMAC(Convert.stringToBytes(data), key);
    }
}

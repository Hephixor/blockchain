package merkle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

	private static String algorithm = "HmacSHA256";
	private static String key = "256bitsKey-ABCDEFGHIJKLMNOPQRSTU";

	/**
	 * Hash un tableau de bytes avec Hmac SHA-256.
	 * @param data tableau de bytes à hasher.
	 * @return hash sur 256 bits à stocker dans un noeud de Merkle Tree.
	 */
	public static byte[] digest(byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return md.digest(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     * Hash une chaîne de charactères en utilisant l'algorithme SHA-256.
     * @param data chaîne de charactères à hasher.
     * @return tableau d'octets représentant un hash sur 256 bits.
     */
	public static byte[] digestString(String data) {
	    return digest(Bytes.fromStr(data));
    }
}

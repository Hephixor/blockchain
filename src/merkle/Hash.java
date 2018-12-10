package merkle;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

	/**
	 * 
	 * @param data tableau de bytes à hasher.
	 * @return hash sur 256 bits à stocker dans un noeud de Merkle Tree.
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] digest(byte[] data) throws NoSuchAlgorithmException {
		return MessageDigest.getInstance("SHA-256").digest(data);		
	}
	
	/**
	 * 
	 * @param data String à hasher.
	 * @return hash sur 256 bits à stocker dans un noeud de Merkle Tree.
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] digest(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return digest(data.getBytes("UTF-8"));
	}
	
	/**
	 * Produit le hash de la concaténation de 2 hashs.
	 * Utilisé pour produire le hash d'un noeud de Merkle à partir des hashs de ses noeuds fils.
	 * @param hash1 tableau de bytes
	 * @param hash2 tableau de bytes
	 * @return hash sur 256 bytes de la concaténation de hash1 et hash2.
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] digest(byte[] hash1, byte[] hash2) throws NoSuchAlgorithmException {
		return digest(concat(hash1, hash2));
	}
	
	/**
	 * https://stackoverflow.com/a/5513188
	 * @param hash1 tableau n bytes
	 * @param hash2 tableau m bytes
	 * @return tableau de n+m bytes concaténation hash1hash2
	 */
	public static byte[] concat(byte[] hash1, byte[] hash2) {
		byte[] hashsConcat = new byte[hash1.length + hash2.length];
		System.arraycopy(hash1, 0, hashsConcat, 0, hash1.length);
		System.arraycopy(hash2, 0, hashsConcat, hash1.length, hash2.length);
		
		return hashsConcat;
	}

	/**
	 * https://stackoverflow.com/a/9855338
	 * @param bytes tableau de bytes[] à afficher.
	 * @return String de 256 bits (64 char) en hexadecimal.
	 */
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	
}

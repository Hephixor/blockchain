package merkle;

import java.io.UnsupportedEncodingException;

public class Bytes {
	
	/**
	 * https://stackoverflow.com/a/5513188
	 * @param tab1 tableau n bytes
	 * @param tab2 tableau m bytes
	 * @return tableau de n+m bytes concaténation tab1tab2
	 */
	public static byte[] concat(byte[] tab1, byte[] tab2) {
		byte[] tabsConcat = new byte[tab1.length + tab2.length];
		System.arraycopy(tab1, 0, tabsConcat, 0, tab1.length);
		System.arraycopy(tab2, 0, tabsConcat, tab1.length, tab2.length);

		return tabsConcat;
	}
	
	/**
	 * https://stackoverflow.com/a/5513188 avec 3 tabs à concaténer.
	 * @param tab1 tableau n bytes
	 * @param tab2 tableau m bytes
	 * @param tab3 tableau o bytes
	 * @return tableau de n+m+o bytes concaténation tab1tab2tab3
	 */
	public static byte[] concat(byte[] tab1, byte[] tab2, byte[] tab3) {
		byte[] tabsConcat = new byte[tab1.length + tab2.length + tab3.length];
		System.arraycopy(tab1, 0, tabsConcat, 0, tab1.length);
		System.arraycopy(tab2, 0, tabsConcat, tab1.length, tab2.length);
		System.arraycopy(tab3, 0, tabsConcat, tab1.length+tab2.length, tab3.length);

		return tabsConcat;
	}
	
	
	/**
	 * https://stackoverflow.com/a/9855338
	 * @param bytes tableau de bytes[] à afficher.
	 * @return String de 256 bits (64 char) en hexadecimal.
	 */
	public static String toHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * fonction équivalent de str.getBytes("UTF-8") sans throws UnsupportedEncodingException.
	 * @param str String
	 * @return byte[] représentant la String donnée.
	 */
	public static byte[] fromStr(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}

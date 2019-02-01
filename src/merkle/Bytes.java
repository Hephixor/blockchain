package merkle;

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
}

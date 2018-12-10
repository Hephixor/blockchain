package tests;

import merkle.Hash;

public class HashTest {
	
	public static void main(String[] args) throws Exception {
		
		String t1 = "Hello World";
		String t2 = "test2";
		String t3 = "Hello World";

		System.out.println(Hash.bytesToHex(Hash.digest(t1)));
		System.out.println(Hash.bytesToHex(Hash.digest(t2)));
		
		System.out.println("\n"+ Hash.bytesToHex(Hash.digest(t3)));
		System.out.println(Hash.bytesToHex(Hash.digest(t3)).length() +" char");
		System.out.println(Hash.digest(t3).length +" bytes");
		
		byte[] concat = Hash.concat(Hash.digest(t1), Hash.digest(t2));
		
		System.out.println("\n"+ Hash.bytesToHex(concat));
		System.out.println(Hash.bytesToHex(concat).length() +" char");
		System.out.println(concat.length +" bytes");
		
		byte[] hashConcat = Hash.digest(Hash.digest(t1), Hash.digest(t2));
		
		System.out.println("\n"+ Hash.bytesToHex(hashConcat));
		System.out.println(Hash.bytesToHex(hashConcat).length() +" char");
		System.out.println(hashConcat.length +" bytes");
		
		byte[][] datas = {t1.getBytes("UTF-8"), t2.getBytes("UTF-8"), t3.getBytes("UTF-8")};
		byte[][] hashs = Hash.digest(datas);
		
		System.out.println(" ");
		for(int i=0; i<hashs.length; i++) {
			System.out.println(Hash.bytesToHex(hashs[i]));
		}
		
		String[] datasString = {t1, t2, t3};
		byte[][] hashsString = Hash.digest(datasString);
		
		System.out.println(" ");
		for(int i=0; i<hashs.length; i++) {
			System.out.println(Hash.bytesToHex(hashsString[i]));
		}
	}

}

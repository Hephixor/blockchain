package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import merkle.Hash;

class HashTest {
	
	final String s1 = "Hello World";
	final String s1bis = "Hello World";
	final String s2 = "test2";

	byte[] b1;
	byte[] b1bis;
	byte[] b2;
	
	@BeforeEach
	public void beforeEach() throws UnsupportedEncodingException {
		b1 = s1.getBytes("UTF-8");
		b1bis = s1bis.getBytes("UTF-8");
		b2 = s2.getBytes("UTF-8");
	}
	
	@AfterEach 
	public void afterEach() {
		b1 = null;
		b1bis = null;
		b2 = null;
	}
	
	@Test
	void testDigestByteArray() {
		byte[] h1 = Hash.digest(b1);
		byte[] h1bis = Hash.digest(b1bis);
		byte[] h2 = Hash.digest(b2);
		
		assertTrue(Arrays.equals(h1, h1bis));
		assertTrue(!Arrays.equals(h1, h2));
		assertTrue(h1.length == 32);
		
		System.out.println("\n/*---------- digest(byte[] data) ----------*/");
		System.out.println("h1: "+ Hash.bytesToHex(h1));
		System.out.println("h1b:"+ Hash.bytesToHex(h1bis));
		System.out.println("h2: "+ Hash.bytesToHex(h2));
	}
	
	@Test
	void testDigestString() {
		byte[] hashExpected = Hash.digest(b1);
		byte[] hashProducted = Hash.digest(s1);
		
		assertTrue(Arrays.equals(hashExpected, hashProducted));
		
		System.out.println("\n/*---------- digest(String data) ----------*/");
		System.out.println("hashExpected: "+ Hash.bytesToHex(hashExpected));
		System.out.println("hashProducted:"+ Hash.bytesToHex(hashProducted));
	}

	@Test
	void testDigestByteArrayByteArray() {
		byte[] hashExpected = Hash.concat(b1, b2);		
		byte[] hashProducted = Hash.digest(b1, b2);
		
		assertTrue(Arrays.equals(hashExpected, hashProducted));
		
		System.out.println("\n/*---------- digest(byte[] hash1, byte[] hash2) ----------*/");
		System.out.println("hashExpected: "+ Hash.bytesToHex(hashExpected));
		System.out.println("hashProducted:"+ Hash.bytesToHex(hashProducted));
	}
}

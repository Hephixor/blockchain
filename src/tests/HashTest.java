package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import merkle.Hash;
import tools.Convert;

class HashTest {
	
	final String s1 = "Hello World";
	final String s1bis = "Hello World";
	final String s2 = "test2";

	final String SHA256_DIR = "test_files/test_crypto/sha256";

	byte[] b1;
	byte[] b1bis;
	byte[] b2;
	
	@BeforeEach
	void beforeEach() throws UnsupportedEncodingException {
		b1 = s1.getBytes("UTF-8");
		b1bis = s1bis.getBytes("UTF-8");
		b2 = s2.getBytes("UTF-8");
	}
	
	@AfterEach 
	void afterEach() {
		b1 = null;
		b1bis = null;
		b2 = null;
	}
	
	@Test
	void testDigestByteArray() {
		byte[] h1 = Hash.digestSHA256(b1);
		byte[] h1bis = Hash.digestSHA256(b1bis);
		byte[] h2 = Hash.digestSHA256(b2);
		
		System.out.println("\n/*---------- digest(byte[] data) ----------*/");
		System.out.println("h1: "+ Convert.bytesToHex(h1));
		System.out.println("h1b:"+ Convert.bytesToHex(h1bis));
		System.out.println("h2: "+ Convert.bytesToHex(h2));
		
		assertTrue(Arrays.equals(h1, h1bis));
		assertTrue(!Arrays.equals(h1, h2));
		assertTrue(h1.length == 32);
	}

	@Test
    void testSHA256File() throws IOException, NoSuchAlgorithmException {
        System.out.println("\n/*---------- hashString(String \"data\") ----------*/");

        byte[] data = Hash.digestSHA256String("data");
        String result = Convert.bytesToHex(data).toLowerCase();
        String expected = new String(Files.readAllBytes(Paths.get(SHA256_DIR, "sha256_hex")), StandardCharsets.UTF_8);

        System.out.println("expected:\t" + expected);
        System.out.println("actual:\t\t" + result);

        assertEquals(expected, result);
    }
}

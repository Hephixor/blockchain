package tests.crypto;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import merkle.Convert;
import merkle.Hash;

public class HmacTest {

	String dir = "./test_files/test_crypto/hmac/";
	
	@Test
	void testValid() throws IOException {
		String data = Convert.fileToString(dir+"valid/data");
		String secret = Convert.fileToString(dir+"valid/secret");
		byte[] hmac = Convert.fileToBytes(dir+"valid/hmac");
		
		byte[] my_hmac = Hash.digestHMACString(data, secret);
		
		assert Arrays.equals(my_hmac, hmac);
	}
	
	@Test
	void testInvalid() throws IOException {
		String data = Convert.fileToString(dir+"invalid/data");
		String secret = Convert.fileToString(dir+"invalid/secret");
		byte[] hmac = Convert.fileToBytes(dir+"invalid/hmac");
		
		byte[] my_hmac = Hash.digestHMACString(data, secret);
		
		assert ! Arrays.equals(my_hmac, hmac);
	}
}

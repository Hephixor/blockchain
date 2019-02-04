package tests.crypto;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import merkle.Hash;
import tools.Convert;

public class HmacTest {

	String dir = "./test_files/test_crypto/hmac/";
	
	@Test
	void testValid() throws IOException {
		byte[] data = Convert.fileToBytes(dir+"valid/data");
		byte[] secret = Convert.fileToBytes(dir+"valid/secret");
		byte[] hmac = Convert.fileToBytes(dir+"valid/hmac");
		
		byte[] my_hmac = Hash.digestHMAC(data, secret);
		
		assert Arrays.equals(my_hmac, hmac);
	}
	
	@Test
	void testInvalid() throws IOException {
		byte[] data = Convert.fileToBytes(dir+"invalid/data");
		byte[] secret = Convert.fileToBytes(dir+"invalid/secret");
		byte[] hmac = Convert.fileToBytes(dir+"invalid/hmac");
		
		byte[] my_hmac = Hash.digestHMAC(data, secret);
		
		assert ! Arrays.equals(my_hmac, hmac);
	}
}

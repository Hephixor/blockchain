package tests.crypto;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import merkle.Hash;
import tools.Convert;

public class Sha256Test {

	String dir = "./test_files/test_crypto/sha256/";
	
	@Test
	void test() throws IOException {
		String data = Convert.fileToString(dir+"data");
		byte[] sha256 = Convert.fileToBytes(dir+"sha256");
		String sha256_hex = Convert.fileToString(dir+"sha256_hex");
		
		byte[] my_sha256 = Hash.digestSHA256String(data);
		
		assert Arrays.equals(my_sha256, sha256);
		assert Convert.bytesToHex(my_sha256).equals(sha256_hex);
	}
}

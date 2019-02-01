package tests.crypto;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import merkle.Convert;

public class HexTest {

	String dir = "./test_files/test_crypto/hex/";
	
	@Test
	void test_0() throws IOException {
		String data = Convert.fileToString(dir + "test_0/data");
		String hex = Convert.fileToString(dir + "test_0/hex");
		
		String my_data = Convert.hexToString(hex);
		String my_hex = Convert.bytesToHex(data.getBytes());
		
		assert my_data.equals(data);
		assert my_hex.equals(hex);
	}
	
	@Test
	void test_1() throws IOException {
		byte[] data = Convert.fileToBytes(dir + "test_1/data");
		String hex = Convert.fileToString(dir + "test_1/hex");
		
		byte[] my_data = Convert.hexToBytes(hex);
		String my_hex = Convert.bytesToHex(data);
		
		assert Arrays.equals(my_data, data);
		assert my_hex.equals(hex);
	}
	
	@Test
	void test_2() throws IOException {
		String data = Convert.fileToString(dir + "test_2/data");
		String hex = Convert.fileToString(dir + "test_2/hex");
		
		String my_data = Convert.hexToString(hex);
		String my_hex = Convert.stringToHex(data);
		
		assert my_data.equals(data);
		assert my_hex.equals(hex);
	}
}

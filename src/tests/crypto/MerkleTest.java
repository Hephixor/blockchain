package tests.crypto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import merkle.Bytes;
import merkle.Convert;
import merkle.Hash;
import merkle.Merkle;

public class MerkleTest {

	String dir = "./test_files/test_crypto/merkle/";
	
	@Test
	void test_Leaf_0() throws IOException {
		String data_00 = Convert.fileToString(dir+"leaf_0/data_00");
		byte[] root = Convert.fileToBytes(dir+"leaf_0/root");
		
//		String[] datas = {data_00};
//		byte[] my_root = Merkle.merkleTree(datas).getHash();
		
		byte[] hash1 = Hash.digestSHA256String(data_00);
		byte[] my_root = Hash.digestSHA256String("1"+Convert.bytesToHex(hash1));
		
		assert Arrays.equals(root, my_root);
	}
	
	@Test
	void test_hash() throws IOException {
		String data_00 = Convert.fileToString(dir+"leaf_0/data_00");
		String root_hex = Convert.fileToString(dir+"leaf_0/root_hex");
		
		String hex_00 = Convert.stringToHex(data_00);
		System.out.println(hex_00);
		System.out.println(Convert.bytesToString(Convert.stringToBytes(hex_00)));
		System.out.println(Convert.bytesToString(Convert.hexToBytes(hex_00)));
		String my_root_hex = Convert.bytesToString(Hash.digestHMAC(
				Convert.stringToBytes("1"+hex_00), "secret"));
		
		
		System.out.println(my_root_hex);
		System.out.println(root_hex);
		assert my_root_hex.equals(root_hex);
	}
	
	void test_Node_1() {
		
	}
}

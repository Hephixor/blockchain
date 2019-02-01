package tests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import merkle.Convert;
import merkle.Hash;
import merkle.Merkle;
import merkle.Node;

public class MerkleTest2 {
	
	String dir = "./test_files/test_crypto/merkle/";
	
	@Test
	void testEmpty() throws IOException {
		byte[] root = Files.readAllBytes(Paths.get(dir + "empty/root"));
		System.out.println(Convert.bytesToHex(root));
		
		
		byte[][] operations = {"".getBytes()};
		byte[] roothash = Merkle.merkleTree(operations).getHash();
		System.out.println(Convert.bytesToHex(roothash));
		
		fail();
		
		//TODO;
	}
	
	
	@Test
	void testLeaf_0() throws IOException {
		String data_00 = new String(Files.readAllBytes(Paths.get(dir + "leaf_0/data_00")));
		System.out.println(data_00);

		String roothash = Files.readAllLines(Paths.get(dir + "leaf_0/root_hex")).get(0);
		System.out.println(roothash);
		
		System.out.println(Convert.bytesToHex(Hash.digest(data_00.getBytes())));
		
		String[] data = {data_00};
		Node merkle = Merkle.merkleTree(data);
		
		System.out.println(Convert.bytesToHex(merkle.getHash()));
	}
	
}

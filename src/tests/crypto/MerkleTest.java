package tests.crypto;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import merkle.Bytes;
import merkle.Convert;
import merkle.Hash;
import merkle.Merkle;

public class MerkleTest {

	String dir = "./test_files/test_crypto/merkle/";
	
	@Test
	void test_Empty() throws IOException {
		byte[] root = Convert.fileToBytes(dir+"empty/root");
		
		String[] datas = {};
		
		byte[] my_root = Merkle.getRootHash(datas);
		
		assert Arrays.equals(root, my_root);
	}
	
	@Test
	void test_Leaf() throws IOException {
		for(int i=0; i<=3; i++) {
			String data_00 = Convert.fileToString(dir+"leaf_"+i+"/data_00");
			byte[] root = Convert.fileToBytes(dir+"leaf_"+i+"/root");
			
			String[] datas = {data_00};
			byte[] my_root = Merkle.getRootHash(datas);
			
			assertTrue("leaf_"+i, Arrays.equals(my_root, root));
		}
	}
	
	@Test
	void test_Node_0_1() throws IOException {
		for(int i=0; i<=1; i++) {
			String data_00 = Convert.fileToString(dir+"node_"+i+"/data_00");
			String data_01 = Convert.fileToString(dir+"node_"+i+"/data_01");
			byte[] root = Convert.fileToBytes(dir+"node_"+i+"/root");
			
			String[] datas = {data_00, data_01};
			byte[] my_root = Merkle.getRootHash(datas);
			
			assertTrue("node_"+i, Arrays.equals(my_root, root));
		}
	}
	
	@Test
	void node_2() throws IOException {
		String data_00 = Convert.fileToString(dir+"node_2/data_00");
		String data_01 = Convert.fileToString(dir+"node_2/data_01");
		String data_02 = Convert.fileToString(dir+"node_2/data_02");
		String data_03 = Convert.fileToString(dir+"node_2/data_03");
		byte[] root = Convert.fileToBytes(dir+"node_2/root");
		
		String[] datas = {data_00, data_01, data_02, data_03};
		byte[] my_root = Merkle.getRootHash(datas);
		
		assert Arrays.equals(my_root, root);
	}
	
	@Test
	void node_3() throws IOException {
		String data_00 = Convert.fileToString(dir+"node_3/data_00");
		String data_01 = Convert.fileToString(dir+"node_3/data_01");
		String data_02 = Convert.fileToString(dir+"node_3/data_02");
		byte[] root = Convert.fileToBytes(dir+"node_3/root");
		
		String[] datas = {data_00, data_01, data_02};
		byte[] my_root = Merkle.getRootHash(datas);
		
		assert Arrays.equals(my_root, root);
	}
	
	@Test
	void node_4() throws IOException {
		String data_00 = Convert.fileToString(dir+"node_4/data_00");
		String data_01 = Convert.fileToString(dir+"node_4/data_01");
		String data_02 = Convert.fileToString(dir+"node_4/data_02");
		String data_03 = Convert.fileToString(dir+"node_4/data_03");
		String data_04 = Convert.fileToString(dir+"node_4/data_04");
		String data_05 = Convert.fileToString(dir+"node_4/data_05");
		String data_06 = Convert.fileToString(dir+"node_4/data_06");
		byte[] root = Convert.fileToBytes(dir+"node_4/root");
		
		String[] datas = {data_00, data_01, data_02, data_03, data_04, data_05, data_06};
		byte[] my_root = Merkle.getRootHash(datas);
		
		assert Arrays.equals(my_root, root);
	}
	
	@Test
	void node_5() throws IOException {
		String data_00 = Convert.fileToString(dir+"node_5/data_00");
		String data_01 = Convert.fileToString(dir+"node_5/data_01");
		String data_02 = Convert.fileToString(dir+"node_5/data_02");
		String data_03 = Convert.fileToString(dir+"node_5/data_03");
		String data_04 = Convert.fileToString(dir+"node_5/data_04");
		String data_05 = Convert.fileToString(dir+"node_5/data_05");
		String data_06 = Convert.fileToString(dir+"node_5/data_06");
		String data_07 = Convert.fileToString(dir+"node_5/data_07");
		String data_08 = Convert.fileToString(dir+"node_5/data_08");
		String data_09 = Convert.fileToString(dir+"node_5/data_09");
		String data_10 = Convert.fileToString(dir+"node_5/data_10");
		String data_11 = Convert.fileToString(dir+"node_5/data_11");
		String data_12 = Convert.fileToString(dir+"node_5/data_12");
		String data_13 = Convert.fileToString(dir+"node_5/data_13");
		String data_14 = Convert.fileToString(dir+"node_5/data_14");
		byte[] root = Convert.fileToBytes(dir+"node_5/root");
		
		String[] datas = {data_00, data_01, data_02, data_03, data_04, 
				data_05, data_06, data_07, data_08, data_09, data_10,
				data_11, data_12, data_13, data_14};
		byte[] my_root = Merkle.getRootHash(datas);
		
		assert Arrays.equals(my_root, root);
	}

}

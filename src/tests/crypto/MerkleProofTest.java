package tests.crypto;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import merkle.Merkle;
import tools.Convert;

public class MerkleProofTest {

	String dir = "./test_files/test_crypto/merkle/";
	
	@Test
	void test_proof_2() throws IOException {
		byte[] root = Convert.fileToBytes(dir+"proof_2/root");
		String data_00 = Convert.fileToString(dir+"proof_2/data_00");
		String data_01 = Convert.fileToString(dir+"proof_2/data_01");
		
		String[] datas = {data_00, data_01};
		byte[] my_root = Merkle.getRootHash(datas);
		
		assert Arrays.equals(my_root, root);
	}
}

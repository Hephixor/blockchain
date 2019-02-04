package tests.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import crypto.SECP256R1;
import tools.Convert;

public class Secp256r1Test {

	String dir = "./test_files/test_crypto/SECP256R1/";
	
	@Test
	void test_MakeSignature() throws Exception {
		byte[] data = Convert.fileToBytes(dir+"data");
		String secretKey = Convert.fileToString(dir+"secret_key_hex");
		byte[] valid_sign = Convert.fileToBytes(dir+"valid_signature");
		byte[] valid_sign2 = Convert.fileToBytes(dir+"valid_signature2");
		String comp_pubKey = Convert.fileToString(dir+"public_key_compressed_hex");
		String uncomp_pubKey = Convert.fileToString(dir+"public_key_uncompressed_hex");
		
		
		
		SECP256R1 crypto = new SECP256R1();
		
		byte[] my_sign = crypto.makeSignature(data, secretKey);
		
		System.out.println(Convert.bytesToHex(my_sign));
		System.out.println(Convert.bytesToHex(valid_sign));
		System.out.println(Convert.bytesToHex(valid_sign2));
		//assert crypto.verifySignature(my_sign, data, comp_pubKey);
		
		//assert crypto.verifySignature(my_sign, data, uncomp_pubKey);
		
	}
}

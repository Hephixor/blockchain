package tests.draft;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bouncycastle.util.Arrays;

import com.google.gson.GsonBuilder;

public class testMerkleTreeWithTransactions {

	public static String filePath = "./test_files/test_chains/multi_announces/more_blocks_per_announce/announce_0/head/block";
	public static void main(String[] args) throws IOException {

		Path fileLocation = Paths.get(filePath);
		
		byte[] data = Files.readAllBytes(fileLocation);
		
		/* [taille (T) sur 32 bits Big Endian du message suivant] +
		 * [ [JSON valide sur (T - taille_signature) octets] + [signature (hash) sur 256 bits] ]
		 */
		
		ByteBuffer wrapped = ByteBuffer.wrap(data);
		
		int taille_T = wrapped.getInt();
		int taille_signature = 64;
		int taille_json = taille_T - taille_signature;
		
		byte jsonBytes[] = Arrays.copyOfRange(data, 4, 4+taille_json);
		
		String json = new String(jsonBytes, StandardCharsets.UTF_8);
		System.out.println(json);
	}

}

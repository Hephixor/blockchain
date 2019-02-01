package merkle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.bouncycastle.util.encoders.Hex;

public class Convert {
	
	
	public static String bytesToString(byte[] bytes) {
		return new String(bytes);
	}
	
	public static String bytesToHex(byte[] bytes) {
		return Hex.toHexString(bytes);
	}

	
	
	public static byte[] stringToBytes(String str) {
		return str.getBytes();
	}
	
	public static String stringToHex(String str) {
		return bytesToHex(stringToBytes(str));
	}
	
	
	
	public static byte[] hexToBytes(String hex) {
		return Hex.decode(hex);
	}

	public static String hexToString(String hex) {
		return bytesToString(hexToBytes(hex));
	}
	
	
	
	public static byte[] fileToBytes(String path) throws IOException {
		return Files.readAllBytes(Paths.get(path));
	}
	
	public static String fileToString(String path) throws IOException {
		return bytesToString(Files.readAllBytes(Paths.get(path)));
	}
}

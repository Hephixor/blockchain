package crypto;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class CryptoUtils {

	public static byte[] makeECDSASignature(PrivateKey privateKey, String input) {
		Signature signature;
		byte[] outputBytes = new byte[0];
		try {
			signature = Signature.getInstance("SHA256withECDSA", "BC");
			signature.initSign(privateKey);
			byte[] inputBytes = input.getBytes();
			signature.update(inputBytes);
			byte[] signtureBytes = signature.sign();
			outputBytes = signtureBytes;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return outputBytes;
	}


	public static boolean verifyECDSASignature(PublicKey publicKey, String data, byte[] signatureBytes) {
		try {
			Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
			signature.initVerify(publicKey);
			signature.update(data.getBytes());
			return signature.verify(signatureBytes);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

}

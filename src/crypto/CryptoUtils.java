package crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class CryptoUtils {

	public static byte[] makeECDSASignature(PrivateKey privateKey, String input) {
		Signature signature;
		byte[] outputBytes = new byte[0];
		try {
			signature = Signature.getInstance("ECDSA", "BC");
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
			Signature signature = Signature.getInstance("ECDSA", "BC");
			signature.initVerify(publicKey);
			signature.update(data.getBytes());
			return signature.verify(signatureBytes);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
}

}

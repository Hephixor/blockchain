package crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
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


	public static boolean verifyECDSASignature(PublicKey publicKey, byte[] data, byte[] signatureBytes) {
		Signature sig;
		try {
			sig = Signature.getInstance("SHA256withECDSA", "BC");
			sig.initVerify(publicKey);
			sig.update(data);
			if (!sig.verify(signatureBytes))
			{
				System.out.println("Fail");
				return false;
			} else {
				System.out.println("Good");
				return true;
			}
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
		return false;


	}

	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

}

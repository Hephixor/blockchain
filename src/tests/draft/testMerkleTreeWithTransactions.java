package tests.draft;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECKeySpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import crypto.Chose;
import crypto.CryptoUtils;
import merkle.Hash;

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

		System.err.println("Taille T: " + taille_T+"\nTaille signature : "+ taille_signature + "\nTaille JSON : "+taille_json);

		// Taille
		System.out.println("\nTaille \n" + taille_T);


		// Message
		byte jsonBytes[] = Arrays.copyOfRange(data, 4, 4+taille_json);
		String json = new String(jsonBytes, StandardCharsets.UTF_8);
		System.out.println("\nBloc \n" + json);

		// Signature
		byte signatureBytes[] = Arrays.copyOfRange(data, 4+taille_json, 4+taille_json+taille_signature);
		String jsonSig = new String(signatureBytes, StandardCharsets.UTF_8);
		System.out.println("\nSIGNATURE\n"+ jsonSig);

		System.err.println("Validating signature ------ \n");

		JSONObject jsonK;
		try {
			jsonK = new JSONObject(json);
			String jsonKStr = jsonK.get("pub-key").toString();
			System.err.println("pub key :" + jsonKStr);
			
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	        KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
	        ECParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
	        ECCurve curve = spec.getCurve();
	        ECPublicKeySpec pubKey = new ECPublicKeySpec(curve.decodePoint(Hex.decode("029e15edf9abdbf2bbcbedad647c881ca6d0a068552f8dc459c4fef3439254103e")),spec);
//	        ECPublicKeySpec pubKey = new ECPublicKeySpec(curve.decodePoint(Hex.decode(jsonKStr)),spec);

	        PublicKey vKey = fact.generatePublic(pubKey);
	        
	        byte msg[] = Hex.decode("736f6d652064617461");
	        
	        byte rawSig[] = Hex.decode("5264964a82b0175b38abf71f396ac0cc5e883845051b5401f98e4448ca4f71ab272d97bd1880e0cdb2021e9444c5ac34c3a93f9d5d93c57394e39ee9a6205bdb");
	        byte sigBytes[] = Chose.asn1_encode(rawSig);

	        System.out.println("signatureBytes " + signatureBytes);
	        System.out.println("rawSig " + rawSig);
	        System.out.println("sigBytes " + sigBytes);
	        
	        
	        System.out.println("Valid signature : "+CryptoUtils.verifyECDSASignature(vKey, msg, sigBytes));

		} catch (JSONException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
		}



	}



}

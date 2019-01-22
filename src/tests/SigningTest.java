package tests;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.junit.jupiter.api.Test;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;

public class SigningTest {
    final Path SECP_DIR = Paths.get("test_files", "test_crypto", "SECP256R1");

    @Test
    public void verifySignature() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] publicKeyData = Files.readAllBytes(SECP_DIR.resolve("public_key_uncompressed"));
        byte[] validSignature = Files.readAllBytes(SECP_DIR.resolve("valid_signature"));
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
        KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
        ECNamedCurveSpec params = new ECNamedCurveSpec("secp256r1", spec.getCurve(), spec.getG(), spec.getN());
        ECPoint point = ECPointUtil.decodePoint(params.getCurve(), publicKeyData);
        ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(point, params);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(publicKey);
        signature.update(validSignature);
        assertTrue(signature.verify(validSignature));
    }
}

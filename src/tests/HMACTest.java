package tests;

import org.junit.jupiter.api.Test;

import merkle.Convert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static junit.framework.Assert.assertEquals;

public class HMACTest {
    final Path HMAC_DIR_VALID = Paths.get("test_files", "test_crypto", "hmac", "valid");
    final String HMAC_SHA256 = "HmacSHA256";

    @Test
    public void testValidHMAC() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("\n/*---------- HMAC - Valid ----------*/");

        byte[] secretKeyBytes = Files.readAllBytes(HMAC_DIR_VALID.resolve("secret"));
        Mac sha256HMAC = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secret = new SecretKeySpec(secretKeyBytes, HMAC_SHA256);
        sha256HMAC.init(secret);
        sha256HMAC.update(Files.readAllBytes(HMAC_DIR_VALID.resolve("data")));

        String mac = Convert.bytesToHex(sha256HMAC.doFinal());
        System.out.println("actual:   " + mac);

        String expected = Convert.bytesToHex(Files.readAllBytes(HMAC_DIR_VALID.resolve("hmac")));
        System.out.println("expected: " + expected);

        assertEquals(expected, mac);
    }
}

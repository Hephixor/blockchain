package crypto;

import java.math.BigInteger;
import java.security.KeyFactory;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.encoders.Hex;
import java.security.Signature;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Chose {

    public static int bigint_encoding_size(byte high_byte) {
        if (high_byte >= 0) {
            return 32;
        } else {
            return 33;
        }
    }

    public static byte[] asn1_encode(byte [] sign) {
        int len_r = bigint_encoding_size(sign[0]);
        int len_s = bigint_encoding_size(sign[0]);
        int len = 6 + len_r + len_s;
        byte [] res = new byte[len];
        res[0] = 0x30;
        res[1] = (byte) (len - 2); // less than 127
        res[2] = 0x02;
        res[3] = (byte) len_r;
        res[4 + len_r] = 0x02;
        res[5 + len_r] = (byte) len_s;
        if (len_r == 33) {
            System.arraycopy(sign, 0, res, 5, 32);
            res[4] = 0x00;
        } else {
            System.arraycopy(sign, 0, res, 4, 32);
        }
        if (len_s == 33) {
            System.arraycopy(sign, 32, res, 7 + len_r, 32);
            res[6 + len_r] = 0x00;
        } else {
            System.arraycopy(sign, 32, res, 6 + len_r, 32);
        }
        return res;
    }

    public static byte[] asn1_decode(byte [] sign) {
        byte [] res = new byte[64];
        int len_r = sign[3];
        if(len_r == 32) {
            System.arraycopy(sign, 4, res, 0, 32);
        } else {
            System.arraycopy(sign, 5, res, 0, 32);
        }
        if(sign[len_r + 5] == 32) {
            System.arraycopy(sign, len_r + 6, res, 32, 32);
        } else {
            System.arraycopy(sign, len_r + 7, res, 32, 32);
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");

        ECParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256r1");
        ECCurve curve = spec.getCurve();

        // Imorter des clefs signature
        ECPrivateKeySpec priKey =
            new ECPrivateKeySpec(new BigInteger("661296e095da29349f26ca9e7eac10d1056729cbe9f85f9af92380ce496f8415", 16),
                                 spec);

        ECPublicKeySpec pubKey =
            new ECPublicKeySpec(
                                curve.decodePoint(Hex.decode("029e15edf9abdbf2bbcbedad647c881ca6d0a068552f8dc459c4fef3439254103e")),
                                spec);
        PrivateKey          sKey = fact.generatePrivate(priKey);
        PublicKey           vKey = fact.generatePublic(pubKey);

        Signature sig = Signature.getInstance("SHA256withECDSA", "BC");

        byte msg[] = Hex.decode("736f6d652064617461");

        // Imorter une signature raw
        byte rawSig[] = Hex.decode("5264964a82b0175b38abf71f396ac0cc5e883845051b5401f98e4448ca4f71ab272d97bd1880e0cdb2021e9444c5ac34c3a93f9d5d93c57394e39ee9a6205bdb");
        byte sigBytes[] = asn1_encode(rawSig);

        // Vérifier une signature
        sig.initVerify(vKey);
        sig.update(msg);
        if (!sig.verify(sigBytes))
        {
            System.out.println("Fail");
        } else {
            System.out.println("Good");
        }

        // Générer une signature
        sig.initSign(sKey);
        sig.update(msg);
        byte[] signed = sig.sign();
        byte raw_signed[] = asn1_decode(signed);
        System.out.println("sign " + new String (Hex.encode(raw_signed)));

        // Revérifier une signature
        sig.initVerify(vKey);
        sig.update(msg);
        if (!sig.verify(signed))
        {
            System.out.println("Fail 2");
        } else {
            System.out.println("Good 2");
        }

        System.out.println("That's all falks");
    }
}
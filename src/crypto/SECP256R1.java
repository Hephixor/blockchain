package crypto;

import java.math.BigInteger;
import java.security.KeyFactory;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.encoders.Hex;

import merkle.Convert;

import java.security.Signature;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SECP256R1 {

	private String mode;
	private String pubKey_Hex;
	private String priKey_Hex;
	
    private KeyFactory fact;
    private ECParameterSpec spec;
    private ECCurve curve;
    private Signature sig;
    
    private PublicKey pubKey;
    private PrivateKey priKey;
    
    public SECP256R1() throws Exception {
    	java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    	fact = KeyFactory.getInstance("ECDSA", "BC");
    	spec = ECNamedCurveTable.getParameterSpec("secp256r1");
    	curve = spec.getCurve();
    	sig = Signature.getInstance("SHA256withECDSA", "BC");
    	
    	mode = null;
    	priKey_Hex = null;
    	pubKey_Hex = null;
    }
    
    public boolean verifySignature(byte[] signature, byte[] data, String publicKeyHex) throws Exception {
    	if(this.pubKey_Hex == null || !this.pubKey_Hex.equals(publicKeyHex)) {
    		this.pubKey_Hex = publicKeyHex;
    		ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(curve.decodePoint(Hex.decode(publicKeyHex)), spec);
        	this.pubKey = fact.generatePublic(pubKeySpec);
    	}
    	
    	if(this.mode == null || !this.mode.equals("verify")) {
    		this.mode = "verify";
    		sig.initVerify(this.pubKey);
    	}

    	sig.update(data);
    	return sig.verify(asn1_encode(signature));
    }

    public byte[] makeSignature(byte[] data, String privateKeyHex) throws Exception {
    	if(this.priKey_Hex == null || !this.priKey_Hex.equals(privateKeyHex)) {
    		this.priKey_Hex = privateKeyHex;
    		ECPrivateKeySpec priKeySpec = new ECPrivateKeySpec(new BigInteger(privateKeyHex, 16), spec);
        	this.priKey = fact.generatePrivate(priKeySpec);
    	}
    	
    	if(this.mode == null || !this.mode.equals("sign")) {
    		this.mode = "sign";
    		sig.initSign(this.priKey);
    	}
    	
        sig.update(data);
        return asn1_decode(sig.sign());
    }

    public static void main(String[] args) throws Exception {
    	byte[] data = Convert.stringToBytes("some data");
    	
    	SECP256R1 crypto = new SECP256R1();
    	String privateKey = "661296e095da29349f26ca9e7eac10d1056729cbe9f85f9af92380ce496f8415";
    	byte[] signature = crypto.makeSignature(data, privateKey);
    	
    	String publicKey = "029e15edf9abdbf2bbcbedad647c881ca6d0a068552f8dc459c4fef3439254103e";
    	boolean verif = crypto.verifySignature(signature, data, publicKey);
    	
    	System.out.println(verif?"Good":"Fail");
    	
    	byte[] signature_to_check = Convert.hexToBytes("5264964a82b0175b38abf71f396ac0cc5e883845051b5401f98e4448ca4f71ab272d97bd1880e0cdb2021e9444c5ac34c3a93f9d5d93c57394e39ee9a6205bdb");
    	boolean verif2 = crypto.verifySignature(signature_to_check, data, publicKey);
    	System.out.println(verif2?"Good 2":"Fail 2");
    }
    
    private static int bigint_encoding_size(byte high_byte) {
        if (high_byte >= 0) {
            return 32;
        } else {
            return 33;
        }
    }

    private static byte[] asn1_encode(byte [] sign) {
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

    private static byte[] asn1_decode(byte [] sign) {
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
}

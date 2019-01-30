package chain;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

import crypto.CryptoUtils;
import merkle.Bytes;
import merkle.Hash;

public class Transaction {
	
	public String transactionIdHash; 
	public PublicKey senderPUK; 
	public PublicKey receiverPRK; 
	public float value; 
	public byte[] signature; 
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	private static int nbGeneratedTransaction = 0;  
	

	public Transaction(PublicKey sender, PublicKey receiver, float value,  ArrayList<TransactionInput> inputs) {
		this.senderPUK = sender;
		this.receiverPRK = receiver;
		this.value = value;
		this.inputs = inputs;
	}
	
	// Generated Transaction Hash
	private String generateHash() {
		nbGeneratedTransaction++; //increase the sequence to avoid 2 identical transactions having the same hash
		
		return Bytes.toHex(Hash.digestString(getStringFromKey(senderPUK) + getStringFromKey(receiverPRK) + Float.toString(value) + nbGeneratedTransaction)).toLowerCase();

	}
	
	// Utils
	private static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = getStringFromKey(senderPUK) + getStringFromKey(receiverPRK) + Float.toString(value)	;
		signature = CryptoUtils.makeECDSASignature(privateKey,data);		
	}
	//Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() {
		String data = getStringFromKey(senderPUK) + getStringFromKey(receiverPRK) + Float.toString(value)	;
		return CryptoUtils.verifyECDSASignature(senderPUK, data, signature);
	}
}
package chain;

import java.security.*;
import crypto.CryptoUtils;
import network.Payload;

public class Transaction {
	
	public int transactionId; 
	public PublicKey senderPUK; 
	public TransactionTypeEnum type;
	public Payload payload; 
	public byte[] signature; 
	
	// Constructor
	public Transaction(PublicKey senderPUK, PrivateKey senderPRI, Payload payload, int id, TransactionTypeEnum type) {
		this.type = type;
		this.transactionId = id;
		this.senderPUK = senderPUK;
		this.payload = payload;
		this.signature = generateSignature(senderPRI);
	}
	

	// Generate Signature with all wanted protocols
	public byte[] generateSignature(PrivateKey privateKey) {
		String data = CryptoUtils.getStringFromKey(senderPUK) + this.payload.toString();
		return CryptoUtils.makeECDSASignature(privateKey,data);		
	}
	// Verify the Signature given the same protocols
	public boolean verifiySignature() {
		String data = CryptoUtils.getStringFromKey(senderPUK) + this.payload.toString();
		return CryptoUtils.verifyECDSASignature(senderPUK, data.getBytes(), signature);
	}
	
	
	public Payload getPayload() {
		return payload;
	}
	
	
	// Onto the real thing
	
	public boolean processTransaction() {
			
			if(verifiySignature() == false) {
				System.out.println("ERROR Transaction Signature could not be verified");
				return false;
			}
			return true;
		}
		
	@Override
	public String toString() {
		String[] payloadtab = {payload.toString()};
		String pstr = "";
		for (String string : payloadtab) {
			pstr += string;
		}
		return "Transaction " + transactionId +" -> type " + type + " payload " + pstr; 
	}
	
	
	
	
	
}
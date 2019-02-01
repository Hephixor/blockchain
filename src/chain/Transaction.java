package chain;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

import crypto.CryptoUtils;
import merkle.Bytes;
import merkle.Hash;

public class Transaction {
	
	public String transactionId; 
	public PublicKey senderPUK; 
	public PublicKey receiverPRK; 
	public String data; 
	public byte[] signature; 
	
	
	private static int nbGeneratedTransaction = 0;  
	

	public Transaction(PublicKey sender, PublicKey receiver, String data) {
		this.senderPUK = sender;
		this.receiverPRK = receiver;
		this.data = data;
	}
	
	// Generated Transaction Hash
	private String generateHash() {
		// change the counter to avoid 2 identical transactions having the same hash
		nbGeneratedTransaction++; 
		
		return Bytes.toHex(Hash.digestString(CryptoUtils.getStringFromKey(senderPUK) + CryptoUtils.getStringFromKey(receiverPRK) + data + nbGeneratedTransaction)).toLowerCase();

	}
	
	// Generate Signature with all wanted protocols
	public void generateSignature(PrivateKey privateKey) {
		String data = CryptoUtils.getStringFromKey(senderPUK) + CryptoUtils.getStringFromKey(receiverPRK) + this.data;
		signature = CryptoUtils.makeECDSASignature(privateKey,data);		
	}
	// Verify the Signature given the same protocols
	public boolean verifiySignature() {
		String data = CryptoUtils.getStringFromKey(senderPUK) + CryptoUtils.getStringFromKey(receiverPRK) + this.data;
		return CryptoUtils.verifyECDSASignature(senderPUK, data.getBytes(), signature);
	}
	
	
	// Onto the real thing
	
	public boolean processTransaction() {
			
			if(verifiySignature() == false) {
				System.out.println("ERROR Transaction Signature could not be verified");
				return false;
			}
					
			
			return true;
		}
		
	
	
	
	
	
}
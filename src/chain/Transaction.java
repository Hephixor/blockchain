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
		
		return Bytes.toHex(Hash.digestString(CryptoUtils.getStringFromKey(senderPUK) + CryptoUtils.getStringFromKey(receiverPRK) + Float.toString(value) + nbGeneratedTransaction)).toLowerCase();

	}
	
	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = CryptoUtils.getStringFromKey(senderPUK) + CryptoUtils.getStringFromKey(receiverPRK) + Float.toString(value)	;
		signature = CryptoUtils.makeECDSASignature(privateKey,data);		
	}
	//Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() {
		String data = CryptoUtils.getStringFromKey(senderPUK) + CryptoUtils.getStringFromKey(receiverPRK) + Float.toString(value)	;
		return CryptoUtils.verifyECDSASignature(senderPUK, data, signature);
	}
	
	
	// Onto the real thing
	
	public boolean processTransaction() {
			
			if(verifiySignature() == false) {
				System.out.println("ERROR Transaction Signature could not be verified");
				return false;
			}
					
			//gather transaction inputs (Make sure they are unspent):
			for(TransactionInput i : inputs) {
				i.UTXO = BlockChainManager.getUTXO().get(i.transactionOutputId);
			}

			//check if transaction is valid:
			if(getInputsValue() < BlockChainManager.minimumTransaction) {
				System.out.println("ERROR amount is to low : " + getInputsValue());
				return false;
			}
			
			//generate transaction outputs:
			float reste = getInputsValue() - value; //get value of inputs then the left over change:
			transactionId = generateHash();
			
			// Send the money to receiver 
			outputs.add(new TransactionOutput( this.receiverPRK, value, transactionId)); 
			
			// Send le reste du solde back to the sender
			outputs.add(new TransactionOutput( this.senderPUK, reste, transactionId)); 		
					
			// Add le reste to UTXOs
			for(TransactionOutput transaction : outputs) {
				BlockChainManager.getUTXO().put(transaction.id , transaction);
			}
			
			//remove transaction inputs from UTXO lists as spent:
			for(TransactionInput i : inputs) {
				if(i.UTXO == null) continue; //if Transaction can't be found skip it 
				BlockChainManager.getUTXO().remove(i.UTXO.id);
			}
			
			return true;
		}
		
	//returns sum of inputs(UTXOs) values
		public float getInputsValue() {
			float total = 0;
			for(TransactionInput i : inputs) {
				if(i.UTXO == null) continue; //if Transaction can't be found skip it 
				total += i.UTXO.value;
			}
			return total;
		}

	//returns sum of outputs:
		public float getOutputsValue() {
			float total = 0;
			for(TransactionOutput o : outputs) {
				total += o.value;
			}
			return total;
	}
	
	
	
	
}
package chain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;

public class Node {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private ArrayList<Transaction> transactions;
 


	public Node(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		generateKeyPair();	
		transactions = new ArrayList<Transaction>();
	}

	// Generate the keypair for current Node
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
			KeyPair keyPair = keyGen.generateKeyPair();
			// Set the public and private keys from the keyPair
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	

	// Sign transaction
	public Transaction signTransaction(Transaction transaction) {
		transaction.generateSignature(privateKey);
		return transaction;
	}
	
	// Add to transactions list
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}
	
	public void displayTransactions() {
		for (Transaction transaction : transactions) {
			System.out.println(transaction.toString());
		}
	}
	
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}


}

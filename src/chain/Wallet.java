package chain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.


	public Wallet(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		generateKeyPair();	
	}

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

	// Return value of the wallet
	public float getBalance() {
		float total = 0;

		// Check for personal Transaction Outputs unspent
		for (Map.Entry<String, TransactionOutput> item: BlockChainManager.getUTXO().entrySet()){
			TransactionOutput UTXO = item.getValue();
			//Check for personal money
			if(UTXO.isMine(publicKey)) { 
				UTXOs.put(UTXO.id,UTXO); 
				total += UTXO.value ; 
			}
		}  
		return total;
	}

	// Generate transaction
	public Transaction generateSendTransaction(PublicKey receiver, float amount ) {
		if(getBalance() < amount) { 
			System.out.println("ERROR not enough money ");
			return null;
		}

		// Generate Transaction Input list
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > amount) break;
		}

		Transaction transaction = new Transaction(publicKey, receiver , amount, inputs);
		transaction.generateSignature(privateKey);

		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		return transaction;
	}
	
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}


}

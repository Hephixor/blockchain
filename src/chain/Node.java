package chain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;

import merkle.Merkle;
import tools.Convert;

public class Node {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private ArrayList<Transaction> transactions;
	private ArrayList<Transaction> pendingTransactions;
	private ArrayList<Block> pendingBlocks;

	public Node(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		generateKeyPair();	
		transactions = new ArrayList<Transaction>();
		pendingTransactions = new ArrayList<Transaction>();
		pendingBlocks = new ArrayList<Block>();
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

	public void addPendingBlock(Block block) {
		pendingBlocks.add(block);
	}

	public void removePendingBlock(int index) {
		pendingBlocks.remove(index);
	}

	public Block getPendingBlock(int index) {
		return pendingBlocks.get(index);
	}

	public boolean pending() {
		return(pendingBlocks.size()!=0);
	}

	public void addPendingTransaction(Transaction transaction) {
		pendingTransactions.add(transaction);
	}

	public void removePendingTransaction(int index) {
		pendingBlocks.remove(index);
	}

	public Transaction getPendingTransaction(int index) {
		return pendingTransactions.get(index);
	}

	public boolean pendingTransaction() {
		return(pendingTransactions.size()!=0);
	}


	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public void displayPendingTransaction() {
		System.err.println(pendingTransactions.size()+" Pending Transaction");
		for (Transaction transaction : pendingTransactions) {
			System.out.println(transaction.toString());
		}
	}

	public void makeBlockFromPendings(Block previousBlock) {
		if(pendingTransactions.size()!=0) {
			ArrayList<String> trs = new ArrayList<String>();
			for (Transaction transaction : pendingTransactions) {
				trs.add(transaction.toString());
			}

			String roothash = Convert.bytesToHex(Merkle.getRootHash(trs));
			Block currentBlock = new Block(previousBlock.getHash(), previousBlock.getLevel()+1, previousBlock.getTime()+1,roothash);
			int i=0;
			while(i<pendingTransactions.size()) {
				Transaction tmpT = pendingTransactions.get(0);
				pendingTransactions.remove(0);
				currentBlock.addTransaction(tmpT);
				transactions.add(tmpT);

			}

			pendingBlocks.add(currentBlock);
		}
	}

	public int getNbPendingBlock() {
		return pendingBlocks.size();
	}

	public int getNbPendingTransactions() {
		return pendingTransactions.size();
	}

	public int getNbTransactions() {
		return transactions.size();
	}


}

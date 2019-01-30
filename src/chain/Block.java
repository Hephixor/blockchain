package chain;

import java.util.ArrayList;
import java.util.Date;

import merkle.Bytes;
import merkle.Hash;
import merkle.Merkle;

public class Block {
	private String hash;
	private String previousHash;
	private String data; 
	private long timeStamp; //milliseconds POSIX time
	private int level; //block level
	private int nonce;
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); 


	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = generateHash();
		this.level = 0;
	}

	public void mineBlock(int difficulty) {
		//TODO change function
		merkleRoot = Merkle.getMerkleRoot(transactions);
		
		String target = getMiningNonce(difficulty); 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = generateHash();
		}
		System.out.println("Block mined with hash : " + hash);
	}
	
	public boolean addTransaction(Transaction transaction) {
		// Check if transaction is valid / not genesis block
		
		if(transaction == null) return false;		
		if((previousHash != "0")) {
			if((transaction.processTransaction() != true)) {
				System.out.println("ERROR Can't add transaction");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction added");
		return true;
}

	private static String getMiningNonce(int difficulty) {
		return new String(new char[difficulty]).replace('\0', '0');
	}

	public String generateHash() {
		return Bytes.toHex(Hash.digestString(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot)).toLowerCase();
	}

	//Getters ....

	public String getHash() {
		return hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public String getData() {
		return data;
	}

	//FOR DEBUG PURPOSE ONLY
	public void setData(String data) {
		this.data=data;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public int getLevel() {
		return level;
	}


}

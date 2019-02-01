package chain;

import java.util.ArrayList;
import java.util.Date;

import merkle.Convert;
import merkle.Hash;
import merkle.Merkle;

public class Block {
	private String hash;
	private String previousHash;
	private String data; 
	private long timeStamp; //milliseconds POSIX time
	private int level; //block level
	private int time; // time step
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); 


	public Block(String previousHash, int level, int time, String rootHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.level = level;
		this.time = time;
		this.merkleRoot = rootHash;
		this.hash = generateHash();
	}

	
	public boolean addTransaction(Transaction transaction) {
		// Check if transaction is valid / not genesis block
		
		if(transaction == null) return false;		
		if((!previousHash.equals("0"))) {
			if((!transaction.processTransaction())) {
				System.out.println("ERROR Can't add transaction");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction added");
		return true;
}

	public String generateHash() {
		return Convert.bytesToHex(Hash.digestSHA256String(previousHash + Long.toString(timeStamp) + merkleRoot)).toLowerCase();
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
	
	public int getTime() {
		return time;
	}
	
	public String getMerkleRoot() {
		return merkleRoot;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setTimestamp(long timestampGenesis) {
		this.timeStamp = timestampGenesis + (this.time * 15);
	}
}

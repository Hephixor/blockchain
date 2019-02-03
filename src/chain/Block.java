package chain;

import java.util.ArrayList;
import java.util.Date;

import merkle.Convert;
import merkle.Hash;

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

	public Block(int level, String hash, String previousHash, long timeStamp, String merkleRoot) {
		this.level = level;
		this.hash = hash;
		this.previousHash = previousHash;
		this.timeStamp = timeStamp;
		this.merkleRoot = merkleRoot;
	}


	public boolean addTransaction(Transaction transaction) {

		if(transaction == null) {
			return false;		
		}
		else {
			if(!transaction.verifiySignature()) {
				return false;
			}
			else {
				transactions.add(transaction);
				System.out.println("Transaction added");
				return true;
			}
		}

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

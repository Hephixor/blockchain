package chain;

import java.util.Date;

import merkle.Bytes;
import merkle.Hash;

public class Block {
	private String hash;
	private String previousHash;
	private String data; //data simple message
	private long timeStamp; //milliseconds POSIX time
	private int level; //block level
	private int nonce;

	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = generateHash();
		this.level = 0;
	}

	public void mineBlock(int difficulty) {
		String target = getMiningNonce(difficulty); //Create a string with difficulty * "0" 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = generateHash();
		}
		System.out.println("Block mined with hash : " + hash);
	}

	private static String getMiningNonce(int difficulty) {
		return new String(new char[difficulty]).replace('\0', '0');
	}

	public String generateHash() {
		return Bytes.toHex(Hash.digestString(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data)).toLowerCase();
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

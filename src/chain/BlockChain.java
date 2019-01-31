package chain;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class BlockChain {
	private static ArrayList<Block> blocks;
	private static int difficulty ;

	public BlockChain() {
		blocks = new ArrayList<Block>();
		difficulty = 5;
	}

	public BlockChain(int miningDifficulty) {
		blocks = new ArrayList<Block>();
		difficulty = miningDifficulty;
	}

	public BlockChain(Block genesis, int miningDifficulty) {
		blocks = new ArrayList<Block>();
		difficulty = miningDifficulty;
		addBlock(genesis);

	}

	//Verify BlockChain integrity.. More to do here
	public Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		int maxLevel = blocks.get(blocks.size()-1).getLevel();
		
		for(int i=1; i < blocks.size(); i++) {
			
			currentBlock = blocks.get(i);
			previousBlock = blocks.get(i-1);

			// Verify block
			if(!currentBlock.getHash().equals(currentBlock.generateHash()) ){
				System.err.println("ERROR current block hash is invalid");
				System.err.println("\nCurrently : " + currentBlock.getHash());
				System.err.println("\nShould be : " + currentBlock.generateHash());
				
				return false;
			}

			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
				System.err.println("ERROR previous block hash is invalid");
				return false;
			}
			
			
			if(!(previousBlock.getLevel()==currentBlock.getLevel()-1)) {
				System.err.println("ERROR previous block level is invalid");
				return false;
			}
			
			if(currentBlock.getLevel()>maxLevel) {
				System.err.println("ERROR misordered levels");
				return false;
			}

			/* if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("ERROR block is not correctly mined");
				return false;
			} */

		}

		return true;
	}

	//Utilities

	public void printJsonChain() {
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blocks);		
		System.out.println("\nBlockchain :\n\n" + blockchainJson);
	}

	public void addBlock(Block blockToAdd) {
		//blockToAdd.mineBlock(difficulty);
		blocks.add(blockToAdd);
	}

	public Block getBlockAtIndex(int index) {
		return blocks.get(index);
	}

	public int getSize() {
		return blocks.size();
	}


}

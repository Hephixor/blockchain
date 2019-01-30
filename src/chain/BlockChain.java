package chain;

import java.util.ArrayList;

import com.google.gson.GsonBuilder;

public class BlockChain {
	private static ArrayList<Block> blocks;

	public BlockChain() {
		blocks = new ArrayList<Block>();
	}

	public BlockChain(Block genesis) {
		blocks = new ArrayList<Block>();
		blocks.add(genesis);
	}

	//Verify BlockChain integrity.. More to do here
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;

		//	System.out.println("Checking " + blocks.size() + " blocks\n");
		for(int i=1; i < blocks.size(); i++) {
			currentBlock = blocks.get(i);
			previousBlock = blocks.get(i-1);


			if(!currentBlock.getHash().equals(currentBlock.generateHash()) ){
				System.out.println("Current Hash not equal");			
				return false;
			}

			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
				System.out.println("Previous Hash not equal");
				return false;
			}
		}
		return true;
	}
	
	//Utilities

	public void printJsonChain() {
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blocks);		
		System.out.println("\nBlockchain :\n\n" + blockchainJson);
	}

	public void addBlock(Block blockToAdd) {
		blocks.add(blockToAdd);
	}

	public Block getBlockAtIndex(int index) {
		return blocks.get(index);
	}

	public int getSize() {
		return blocks.size();
	}


}

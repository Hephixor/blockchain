package chain;

import java.util.ArrayList;
import java.util.HashMap;

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
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); 
		
		tempUTXOs.put(BlockChainManager.genesisTransaction.outputs.get(0).id, BlockChainManager.genesisTransaction.outputs.get(0));

		for(int i=1; i < blocks.size(); i++) {

			currentBlock = blocks.get(i);
			previousBlock = blocks.get(i-1);

			// Verify block
			if(!currentBlock.getHash().equals(currentBlock.generateHash()) ){
				System.out.println("ERROR current block hash is invalid");
				return false;
			}
		
			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
				System.out.println("ERROR previous block hash is invalid");
				return false;
			}
			
			if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("ERROR block is not correctly mined");
				return false;
			}

			// Verify Transactions list
			TransactionOutput outputTransaction;
			for(int t=0; t <currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);

				if(!currentTransaction.verifiySignature()) {
					System.out.println("ERROR invalid signture (Transaction #"+t+")");
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("ERROR input/output mismatch (Transaction #"+t+")");
					return false; 
				}

				// Verify inputs
				for(TransactionInput input: currentTransaction.inputs) {	
					outputTransaction = tempUTXOs.get(input.transactionOutputId);

					if(outputTransaction == null) {
						System.out.println("ERROR no input for output (Transaction #"+t+")");
						return false;
					}

					if(input.UTXO.value != outputTransaction.value) {
						System.out.println("ERROR input is invalid (Tansaction #"+t+")");
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				// Verify outputs
				for(TransactionOutput output: currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if( currentTransaction.outputs.get(0).receiver != currentTransaction.receiverPRK) {
					System.out.println("ERROR receiver mismatch (Transaction #"+t+")");
					return false;
				}
				if( currentTransaction.outputs.get(1).receiver != currentTransaction.senderPUK) {
					System.out.println("ERROR sender mismatch (Transaction #"+t+")");
					return false;
				}

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
		blockToAdd.mineBlock(difficulty);
		blocks.add(blockToAdd);
	}

	public Block getBlockAtIndex(int index) {
		return blocks.get(index);
	}

	public int getSize() {
		return blocks.size();
	}


}

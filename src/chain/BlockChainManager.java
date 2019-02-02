package chain;
import java.io.IOException;
import java.util.*;

import org.json.JSONObject;

import crypto.CryptoUtils;
import merkle.Convert;
import merkle.Merkle;
import network.JsonUtils;
import network.PayloadCreation;
import server.Server;
import server.ConcurrentBlockChain;

public class BlockChainManager {
	// First transaction to inject money into blockChain
	public static Transaction genesisTransaction;

	public static BlockChain blockChain;
	public static ConcurrentBlockChain concurrentBlockChain;
	public static Server server;
	public static Node me;
	
	public BlockChainManager() throws IOException {
					// Create BlockChain
					blockChain = new BlockChain();		
					concurrentBlockChain = new ConcurrentBlockChain(blockChain);
					server = new Server(new ArrayList<>(), concurrentBlockChain);
					me = new Node();
					System.out.println("Server started listening on port "+server.getDefaultPort());
	}

	public static Block createNewBlock(String hash) {
		Block newBlock;
		if(blockChain.getSize()==0) {
			newBlock = new Block("0",0,0, hash);
		}
		else {
			Block prevBlock = blockChain.getBlockAtIndex(blockChain.getSize()-1);
			newBlock = new Block(prevBlock.getHash(), prevBlock.getLevel() + 1, prevBlock.getTime() + 1, hash);
		}

		return newBlock;
	}
	
	public String[] makeRegisterTransaction(String event_hash) {
		// Register transaction
		JSONObject jsonRegister = new JSONObject();
		jsonRegister = JsonUtils.makeJson(CryptoUtils.getStringFromKey(me.getPublicKey()), null, "EventHash");
		System.out.println("\n===== Event Register Json Created \n");
		String transaction[] = {jsonRegister.toString()};
		return transaction;
	}
	
	public String[] makeCreateTransaction(PayloadCreation payload) {
		JSONObject jsonPayload = new JSONObject();
		jsonPayload = JsonUtils.makeJson(CryptoUtils.getStringFromKey(me.getPublicKey()), payload, "0");
		System.out.println("\n===== Event Creation Json ===== \n");
		String transaction[] = {jsonPayload.toString()};
		return transaction;
	}
	
	
	public static Block makeBlockOutOfTransaction(String transaction[]) {
		String roothash = Convert.bytesToHex(Merkle.getRootHash(transaction));
		Block block = createNewBlock(roothash);
		return block;
	}
	
	public void makeGenesis() {
		String roothash = "0" ;
		Block genesis = createNewBlock(roothash);
		String[] empty = new String[0];
		JSONObject jsonBlock = JsonUtils.makeJsonBloc(me.getPublicKey(), genesis.getHash(), genesis.getMerkleRoot(), genesis.getLevel(), genesis.getTime());
		blockChain.addBlock(genesis);
		System.out.println(" ==== Genesis Block Created");
	}
	
	public boolean isChainValid() {
		return blockChain.isChainValid();
	}
	
	public static void addBlockToBlockChain(Block b) {
		blockChain.addBlock(b);
	}
	
	public void stopServer() {
		server.stop();
	}

	public static Transaction getGenesisTransaction() {
		return genesisTransaction;
	}

	public static BlockChain getBlockChain() {
		return blockChain;
	}

	public static ConcurrentBlockChain getConcurrentBlockChain() {
		return concurrentBlockChain;
	}

	public static Server getServer() {
		return server;
	}

	public static Node getMe() {
		return me;
	}

	public void displayChain() {
		blockChain.printJsonChain();
	}

}

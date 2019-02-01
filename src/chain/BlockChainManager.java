package chain;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import chain.Block;
import chain.BlockChain;
import chain.Transaction;
import crypto.CryptoUtils;
import merkle.Bytes;
import merkle.Convert;
import merkle.Merkle;
import chain.Node;
import network.JsonUtils;
import network.PayloadCreation;
import server.IpAddress;
import server.Server;
import server.ServerBlockChain;

public class BlockChainManager {
	// First transaction to inject money into blockChain
	public static Transaction genesisTransaction;

	public static BlockChain blockChain;
	public static ServerBlockChain serverBlockChain;
	public static Server server;
	public static Node me;
	
	public BlockChainManager() throws IOException {
					// Create BlockChain
					blockChain = new BlockChain();		
					serverBlockChain = new ServerBlockChain(blockChain);
					server = new Server(serverBlockChain, new ArrayList<>());
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
		String roothash = Convert.bytesToHex(Merkle.merkleTree(transaction).getHash());
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

	public static ServerBlockChain getServerBlockChain() {
		return serverBlockChain;
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

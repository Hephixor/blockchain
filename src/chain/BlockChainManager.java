package chain;
import java.io.IOException;
import java.util.*;

import org.json.JSONObject;

import crypto.CryptoUtils;
import merkle.Convert;
import merkle.Merkle;
import network.JsonUtils;
import network.PayloadCreation;
import network.PayloadRegister;
import server.Server;
import server.ConcurrentBlockChain;

public class BlockChainManager {
	// First transaction to inject money into blockChain
	public static Transaction genesisTransaction;

	public static BlockChain blockChain;
	public static ConcurrentBlockChain concurrentBlockChain;
	public static Server server;
	public static Node me;
	public static Block currentBlock;
	private static int nbGeneratedTransaction = 0; 

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
		
		// Si pas de genesis block
		if(blockChain.getSize()==0) {
			newBlock = new Block("0",0,0, hash);
		}
		// Sinon on ajoute le block
		else {
			Block prevBlock = blockChain.getBlockAtIndex(blockChain.getSize()-1);
			newBlock = new Block(prevBlock.getHash(), prevBlock.getLevel() + 1, prevBlock.getTime() + 1, hash);
		}

		return newBlock;
	}
	
	public Transaction signTransaction(Transaction transaction) {
		return me.signTransaction(transaction);
	}
	
	public void addTransaction(Transaction transaction) {
		me.addTransaction(transaction);
	}

	public Transaction makeRegisterTransaction(PayloadRegister payload) {
		Transaction transaction = new Transaction(me.getPublicKey(), me.getPrivateKey(), payload, ++nbGeneratedTransaction , TransactionTypeEnum.REGISTER);
		return transaction;
	}

	public Transaction makeCreateTransaction(PayloadCreation payload) {
		Transaction transaction = new Transaction(me.getPublicKey(), me.getPrivateKey(), payload, ++nbGeneratedTransaction, TransactionTypeEnum.CREATION);
		return transaction;
	}

	public String[] makeRegisterTransactionStr(PayloadRegister payload) {
		// Register transaction
		JSONObject jsonRegister = new JSONObject();
		jsonRegister = JsonUtils.makeJson(CryptoUtils.getStringFromKey(me.getPublicKey()), null, payload.getEventHash());
		System.out.println("\n===== Event Register Json Created \n");
		String transaction[] = {jsonRegister.toString()};
		return transaction;
	}
	
	public String[] makeRegisterTransactionStrFromTransaction(Transaction transaction) {
		// Register transaction
		JSONObject jsonRegister = new JSONObject();
		jsonRegister = JsonUtils.makeJson(CryptoUtils.getStringFromKey(me.getPublicKey()), null, ((PayloadRegister) transaction.getPayload()).getEventHash());
		System.out.println("\n===== Event Register Json Created \n");
		String transactionStr[] = {jsonRegister.toString()};
		return transactionStr;
	}

	public String[] makeCreateTransactionStr(PayloadCreation payload) {
		JSONObject jsonCreation = new JSONObject();
		jsonCreation = JsonUtils.makeJson(CryptoUtils.getStringFromKey(me.getPublicKey()), payload, "0");
		System.out.println("\n===== Event Creation Json ===== \n");
		String transaction[] = {jsonCreation.toString()};
		return transaction;
	}
	
	public String[] makeCreateTransactionStrFromTransaction(Transaction transaction) {
		JSONObject jsonCreation = new JSONObject();
		jsonCreation = JsonUtils.makeJson(CryptoUtils.getStringFromKey(me.getPublicKey()), (PayloadCreation) transaction.getPayload(), "0");
		System.out.println("\n===== Event Creation Json ===== \n");
		String transactionStr[] = {jsonCreation.toString()};
		return transactionStr;
	}


	public static Block makeBlockOutOfTransaction(Transaction transaction) {
		String[] transactionStr = {transaction.toString()};
		String roothash = Convert.bytesToHex(Merkle.getRootHash(transactionStr));
		Block block = createNewBlock(roothash);
		block.addTransaction(transaction);
		return block;
	}
	
	public static Block makeBlockOutOfTransactionStr(String transaction[]) {
		String roothash = Convert.bytesToHex(Merkle.getRootHash(transaction));
		Block block = createNewBlock(roothash);
		return block;
	}

	public void makeGenesis() {
		String roothash = "0" ;
		Block genesis = createNewBlock(roothash);
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

	public Node getMe() {
		return me;
	}

	public int getNextId() {
		return ++nbGeneratedTransaction;
	}
	
	public void displayChain() {
		blockChain.printJsonChain();
	}
	
	public void pushBlock() {
		addBlockToBlockChain(currentBlock);
	}
	
	public void addTransactionToNode(Transaction transaction) {
		me.addTransaction(transaction);
	}

}

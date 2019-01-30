package chain;
import java.io.IOException;
import java.util.HashMap;

import chain.Block;
import chain.BlockChain;
import chain.Transaction;
import chain.TransactionOutput;
import chain.Wallet;
import crypto.CryptoUtils;
import server.Server;
import server.ServerBlockChain;

public class BlockChainManager {
	// List of unspent transactions == balance of every wallet
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); 
	// BitCoin difficulty is currently ~5814661935891 (yes 5814 Billions) as we are not chinese farmers let's go with 5
	final static int difficulty = 5; // 1 to 10 seconds per block
	
	// Minimum wage for transaction (to be configured with payload ?)
	public static final float minimumTransaction = 0;
	
	// Maximum wage for transaction (to be configured with payload ?)
	public static final float maximumTransaction = 1000;
	
	// First transaction to inject money into blockChain
	public static Transaction genesisTransaction;
	
	public static void main(String[] args) {
		
		try {
			
			// Create BlockChain
			BlockChain blockChain = new BlockChain(difficulty);		
			ServerBlockChain serverBlockChain = new ServerBlockChain(blockChain);

			// Create Wallets
			Wallet myWallet = new Wallet();
			Wallet yourWallet = new Wallet();
			Wallet genesisWallet = new Wallet();
			
			myWallet.generateKeyPair();
			yourWallet.generateKeyPair();
			//System.out.println("Wallet1 Public Key : " + CryptoUtils.getStringFromKey(myWallet.getPublicKey()) + "\n\tPrivate Key : " + CryptoUtils.getStringFromKey(myWallet.getPrivateKey()));
			//System.out.println("\nWallet2 Public Key : " + CryptoUtils.getStringFromKey(yourWallet.getPublicKey()) + "\n\tPrivate Key : " + CryptoUtils.getStringFromKey(yourWallet.getPrivateKey()));

			// Inject money into blockchain with first Transaction 
			// Note that there is it hardcoded that's why we do not generate key pair for genesis wallet 
			genesisTransaction = new Transaction(genesisWallet.getPublicKey(), myWallet.getPublicKey(), 10000, null);
			genesisTransaction.generateSignature(genesisWallet.getPrivateKey());	
			genesisTransaction.transactionId = "0"; 
			genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.receiverPRK, genesisTransaction.value, genesisTransaction.transactionId)); 
			UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); 
			Block genesis = new Block("0");
			genesis.addTransaction(genesisTransaction);
			blockChain.addBlock(genesis);
			System.out.println("\nGenesis Wallet -> MyWallet (10000)");
			
			
			Server server = new Server(serverBlockChain);
			System.out.println("Server started listening on port "+server.DEFAULT_PORT);

			//testing
			Block first = new Block(genesis.getHash());
			System.out.println("\nMyWallet value : " + myWallet.getBalance());
			System.out.println("MyWallet -> YourWallet (100) ");
			first.addTransaction(myWallet.generateSendTransaction(yourWallet.getPublicKey(), 100));
			blockChain.addBlock(first);
			System.out.println("\nMyWallet value : " + myWallet.getBalance());
			System.out.println("YourWallet value : " + yourWallet.getBalance());
			
			Block second = new Block(first.getHash());
			System.out.println("\nMyWallet -> YourWallet (1000000000) ");
			second.addTransaction(myWallet.generateSendTransaction(yourWallet.getPublicKey(), 1000000000));
			blockChain.addBlock(second);
			System.out.println("\nMyWallet value : " + myWallet.getBalance());
			System.out.println("YourWallet value : " + yourWallet.getBalance());
			
			Block third = new Block(second.getHash());
			System.out.println("\nYourWallet -> MyWallet (50) ");
			third.addTransaction(yourWallet.generateSendTransaction(myWallet.getPublicKey(), 50));
			blockChain.addBlock(third);
			System.out.println("\nMyWallet value : " + myWallet.getBalance());
			System.out.println("YourWallet value : " + yourWallet.getBalance());
			

			//blockChain.printJsonChain();

			System.out.println("\nThe blockChain is valid : " + blockChain.isChainValid());
			
			server.stop();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public static HashMap<String,TransactionOutput> getUTXO(){
		return UTXOs;
	}
}

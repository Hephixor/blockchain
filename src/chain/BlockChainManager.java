package chain;
import java.io.IOException;
import chain.Block;
import chain.BlockChain;
import chain.Transaction;
import chain.Wallet;
import server.Server;
import server.ServerBlockChain;

public class BlockChainManager {
// BitCoin difficulty is currently ~5814661935891 (yes 5814 Billions) as we are not chinese farmers let's go with 5
	final static int difficulty = 5; // 1 to 10 seconds per block
	
	// Minimum wage for transaction (to be configured with payload ?)
	public static final float minimumParticipant = 0;
	
	// Maximum wage for transaction (to be configured with payload ?)
	public static final float maximumParticipant = 10;
	
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
			genesisTransaction = new Transaction(genesisWallet.getPublicKey(), myWallet.getPublicKey(), "genesis");
			genesisTransaction.generateSignature(genesisWallet.getPrivateKey());	
			genesisTransaction.transactionId = "0"; 
			Block genesis = new Block("0");
			genesis.addTransaction(genesisTransaction);
			blockChain.addBlock(genesis);
				
			
			Server server = new Server(serverBlockChain);
			System.out.println("Server started listening on port "+server.getDefaultPort());

			//testing
			Block first = new Block(genesis.getHash());
			first.addTransaction(myWallet.generateSendTransaction(yourWallet.getPublicKey(), "second"));
			blockChain.addBlock(first);

			
			Block second = new Block(first.getHash());
			second.addTransaction(myWallet.generateSendTransaction(yourWallet.getPublicKey(), "third"));
			blockChain.addBlock(second);

			
			Block third = new Block(second.getHash());
			third.addTransaction(yourWallet.generateSendTransaction(myWallet.getPublicKey(), "fourth"));
			blockChain.addBlock(third);

			

			blockChain.printJsonChain();

			System.out.println("\nThe blockChain is valid : " + blockChain.isChainValid());
			
			server.stop();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

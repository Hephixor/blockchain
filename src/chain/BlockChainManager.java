package chain;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import chain.Block;
import chain.BlockChain;
import chain.Transaction;
import crypto.CryptoUtils;
import merkle.Merkle;
import chain.Miner;
import network.JsonUtils;
import network.PayloadCreation;
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

	public static BlockChain blockChain;

	public static void main(String[] args) {

		try {

			// Create BlockChain
			blockChain = new BlockChain(difficulty);		
			ServerBlockChain serverBlockChain = new ServerBlockChain(blockChain);
			Server server = new Server(serverBlockChain);
			System.out.println("Server started listening on port "+server.getDefaultPort());

			// Create Wallets
			/*	Wallet myWallet = new Wallet();
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

			System.out.println("\nThe blockChain is valid : " + blockChain.isChainValid()); */

			Miner me = new Miner();



			try {
				// Creation transaction
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				Date today = cal.getTime();        
				String dateBegin = df.format(today);
				cal.add(Calendar.DAY_OF_MONTH, 7);
				String dateEndSub = df.format(today);
				cal.add(Calendar.DAY_OF_MONTH, 7);
				String dateEnd = df.format(today);

				PayloadCreation cinemaPayload = new PayloadCreation("cinema","allons au cinema","Gobelins", dateBegin, dateEndSub, dateEnd, 1 , -1 );
				System.out.println(cinemaPayload.toString());

				JSONObject jsonPayload = new JSONObject();
				jsonPayload = JsonUtils.makeJson(CryptoUtils.getStringFromKey(me.getPublicKey()), cinemaPayload, "0");
				System.out.println("\n===== Event Creation Json ===== \n");
				System.out.println(jsonPayload.toString(3));

				// Register transaction
				JSONObject jsonRegister = new JSONObject();
				jsonRegister = JsonUtils.makeJson(CryptoUtils.getStringFromKey(me.getPublicKey()), null, "EventHash");
				System.out.println("\n===== Event Register Json ===== \n");
				System.out.println(jsonRegister.toString(3));

				// Genesis block
				System.out.println("\n===== Bloc Json ===== \n");
				Block genesis = createNewBlock();
				blockChain.addBlock(genesis);
				String[] empty = new String[0];
				JSONObject jsonBlock = JsonUtils.makeJsonBloc(me.getPublicKey(), genesis.getHash(), genesis.getMerkleRoot(), genesis.getLevel(), genesis.getTime());
				System.out.println(jsonBlock.toString(3));
				
				// 2nd block
				System.out.println("\n===== Bloc Json 2 ===== \n");
				Block second = createNewBlock();
				blockChain.addBlock(second);
				jsonBlock = JsonUtils.makeJsonBloc(me.getPublicKey(), second.getHash(), second.getMerkleRoot(), second.getLevel(), second.getTime());
				System.out.println(jsonBlock.toString(3));

				System.err.println("\n Is blockchain valid : " + blockChain.isChainValid());
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}


			server.stop();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Block createNewBlock() {
		Block newBlock;
		if(blockChain.getSize()==0) {
			newBlock = new Block("0",0,0);
		}
		else {
			Block prevBlock = blockChain.getBlockAtIndex(blockChain.getSize()-1);
			newBlock = new Block(prevBlock.getHash(), prevBlock.getLevel() + 1, prevBlock.getTime() + 1);
		}

		return newBlock;
	}

}

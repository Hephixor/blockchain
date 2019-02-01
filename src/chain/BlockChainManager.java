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
import merkle.Bytes;
import merkle.Merkle;
import chain.Miner;
import network.JsonUtils;
import network.PayloadCreation;
import server.Server;
import server.ServerBlockChain;

public class BlockChainManager {
	// First transaction to inject money into blockChain
	public static Transaction genesisTransaction;

	public static BlockChain blockChain;

	public static void main(String[] args) {

		try {

			// Create BlockChain
			blockChain = new BlockChain();		
			ServerBlockChain serverBlockChain = new ServerBlockChain(blockChain);
			Server server = new Server(serverBlockChain);
			System.out.println("Server started listening on port "+server.getDefaultPort());
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
				String transactions[] = {jsonPayload.toString()};
				String roothash = Bytes.toHex(Merkle.merkleTree(transactions).getHash());
				Block genesis = createNewBlock(roothash);
				blockChain.addBlock(genesis);
				String[] empty = new String[0];
				JSONObject jsonBlock = JsonUtils.makeJsonBloc(me.getPublicKey(), genesis.getHash(), genesis.getMerkleRoot(), genesis.getLevel(), genesis.getTime());
				System.out.println(jsonBlock.toString(3));
				
				// 2nd block
				System.out.println("\n===== Bloc Json 2 ===== \n");
				String transactions2[] = {jsonRegister.toString()};
				roothash = Bytes.toHex(Merkle.merkleTree(transactions2).getHash());
				Block second = createNewBlock(roothash);
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

}

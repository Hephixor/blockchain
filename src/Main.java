import java.io.IOException;
import chain.Block;
import chain.BlockChain;
import chain.Transaction;
import chain.Wallet;
import server.Server;
import server.ServerBlockChain;

public class Main {

	public static void main(String[] args) {
		// BitCoin difficulty is currently ~5814661935891 (yes 5814 Billions) as we are not chinese farmers let's go with 5
		final int difficulty = 5; // 1 to 10 seconds per block

		// Create BlockChaine
		Block genesis = new Block("data","0");
		BlockChain blockChain = new BlockChain(genesis);		
		ServerBlockChain serverBlockChain = new ServerBlockChain(blockChain);

		// Create Wallets
		Wallet myWallet = new Wallet();
		Wallet yourWallet = new Wallet();
		myWallet.generateKeyPair();
		yourWallet.generateKeyPair();
		System.out.println("Wallet1 Public Key : " + myWallet.getStringFromKey(myWallet.getPublicKey()) + "\n\tPrivate Key : " + myWallet.getStringFromKey(myWallet.getPrivateKey()));
		System.out.println("\nWallet2 Public Key : " + yourWallet.getStringFromKey(yourWallet.getPublicKey()) + "\n\tPrivate Key : " + yourWallet.getStringFromKey(yourWallet.getPrivateKey()));

		// Create Transaction
		Transaction transaction = new Transaction(myWallet.getPublicKey(), yourWallet.getPublicKey(), 5, null);
		transaction.generateSignature(myWallet.getPrivateKey());
		
		// Verify the signature works and verify it from the public key
		System.out.println("Signature is valid : " + transaction.verifiySignature());

		try {
			Server server = new Server(serverBlockChain);
			System.out.println("Server started listening on port "+server.DEFAULT_PORT);

			System.out.println("Trying to Mine block 1... ");
			blockChain.getBlockAtIndex(0).mineBlock(difficulty);


			Block second = new Block("second",blockChain.getBlockAtIndex(blockChain.getSize()-1).getHash());
			blockChain.addBlock(second);
			System.out.println("Trying to Mine block 2... ");
			blockChain.getBlockAtIndex(1).mineBlock(difficulty);



			Block third = new Block("third",blockChain.getBlockAtIndex(blockChain.getSize()-1).getHash());
			blockChain.addBlock(third);
			System.out.println("Trying to Mine block 3... ");
			blockChain.getBlockAtIndex(2).mineBlock(difficulty);


			/*
			 for(int i = 0 ; i < blockChain.getSize() ; i++) {
				System.out.println("Block "+i+" Hash: " + blockChain.getBlockAtIndex(i).getHash());
			}
			 */

			blockChain.printJsonChain();

			System.out.println("\n======= TESTING BLOCKCHAIN INTEGRITY ======= \n");
			System.out.println("The blockChaine is valid : " + blockChain.isChainValid());
			System.out.println("Changing a block");
			blockChain.getBlockAtIndex(1).setData("EVIL BLOCK AHAHAHAH");
			System.out.println("The blockChaine is valid : " + blockChain.isChainValid() +"\n");
			//Works for every block changed except genesis


			server.stop();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

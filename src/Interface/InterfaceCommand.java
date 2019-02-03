package Interface;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import chain.Block;
import chain.BlockChainManager;
import chain.Transaction;
import chain.TransactionTypeEnum;
import network.PayloadCreation;
import network.PayloadRegister;

public class InterfaceCommand {
	public static BlockChainManager blockChainManager;

	public static void main(String[] args) {
		try {
			blockChainManager = new BlockChainManager();
		} catch (IOException e) {e.printStackTrace();}
		boolean run = true;
		Scanner sc = new Scanner(System.in);

		do {
			displayMenu();

			if(sc.hasNextInt()) {
				switch(sc.nextInt()) {
				case 1:
					blockChainManager.displayChain();
					break;

				case 2:
					// Creation transaction
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					Date today = cal.getTime();        
					String dateBegin = df.format(today);
					cal.add(Calendar.DAY_OF_MONTH, 7);
					String dateEndSub = df.format(today);
					cal.add(Calendar.DAY_OF_MONTH, 7);
					String dateEnd = df.format(today);
					
					System.out.print(" Nom : ");
					String nom = sc.next();
					System.out.print("\n Description : ");
					String description = sc.next();
					System.out.println("\n Lieu : ");
					String lieu = sc.next();
					System.out.println("\n Date debut / fin /end sub hardcoded");
					System.out.println("\n Min participant : ");
					int min = sc.nextInt();
					System.out.println("\nMax participant : ");
					int max = sc.nextInt();
					
					// Make payload from data
					PayloadCreation payloadC = new PayloadCreation(nom,description,lieu, dateBegin, dateEndSub, dateEnd, min , max);
					
					// Make transaction from payload
					Transaction transactionC = new Transaction(blockChainManager.getMe().getPublicKey(), blockChainManager.getMe().getPrivateKey(), payloadC, blockChainManager.getNextId(),TransactionTypeEnum.CREATION);
					
					// Make block from transaction
					Block creationB = BlockChainManager.makeBlockOutOfTransaction(transactionC);
					
					// Add block to blockChain
					BlockChainManager.addBlockToBlockChain(creationB);
					
					break;

				case 3:
					
					// Make payload from data
					// MISSING EVENT HASH
					PayloadRegister payloadR = new PayloadRegister("EventHash");
					
					// Make transaction from payload
					Transaction transactionR = new Transaction(blockChainManager.getMe().getPublicKey(), blockChainManager.getMe().getPrivateKey(), payloadR, blockChainManager.getNextId(), TransactionTypeEnum.REGISTER); 
					
					// Make block from transaction
					Block registerB = BlockChainManager.makeBlockOutOfTransaction(transactionR);
					BlockChainManager.addBlockToBlockChain(registerB);
					break;
				

				case 4:
					System.out.println("4");
					run = false;
					break;

				case 5:
					blockChainManager.makeGenesis();
					blockChainManager.displayChain();
					break;
				
				case 6:
					System.out.println("BlockChain is valid : " + blockChainManager.isChainValid());
					
				case 7:
					blockChainManager.pushBlock();
					
					
				default:
					break;
				}
			}
			else {break;}


		}while(run);

		sc.close();
		blockChainManager.stopServer();
		System.exit(0);

	}	

	public static void displayMenu() {
		System.out.println(" ========== BlockChain Manager Interface ========== \n");
		System.out.println(" 1. View Blockchain ");
		System.out.println(" 2. Create Event ");
		System.out.println(" 3. Join Event ");
		System.out.println(" 4. Quit ");
		System.out.println(" 5. Add Genesis ");
		System.out.println(" 6. Verify integrity ");
		System.out.println(" 7. Push Block ");
		System.out.println(" ================================================== \n");
	}

}

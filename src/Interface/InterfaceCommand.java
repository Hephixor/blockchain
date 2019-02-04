package Interface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import chain.BlockChainManager;
import chain.Transaction;
import chain.TransactionTypeEnum;
import network.JsonUtils;
import network.PayloadCreation;
import network.PayloadRegister;

public class InterfaceCommand {
	public static BlockChainManager blockChainManager;

	public static void main(String[] args) {
	    blockChainManager = new BlockChainManager();
		boolean run = true;
		Scanner sc = new Scanner(System.in);

		do {
			displayMenu();

			switch(Integer.parseInt(sc.next())) {
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

				// Add transaction to list
				blockChainManager.addPendingTransaction(transactionC);

				// Make block from transaction
				//Block creationB = BlockChainManager.makeBlockOutOfTransaction(transactionC);

				// Add block to pending blocks
				//blockChainManager.addPendingBlock(creationB);

				break;

			case 3:

				// Make payload from data
				// MISSING EVENT HASH
				PayloadRegister payloadR = new PayloadRegister("EventHash");

				// Make transaction from payload
				Transaction transactionR = new Transaction(blockChainManager.getMe().getPublicKey(), blockChainManager.getMe().getPrivateKey(), payloadR, blockChainManager.getNextId(), TransactionTypeEnum.REGISTER); 

				// Add transaction to list
				blockChainManager.addPendingTransaction(transactionR);

				// Make block from transaction
				//Block registerB = BlockChainManager.makeBlockOutOfTransaction(transactionR);
				//blockChainManager.addPendingBlock(registerB);

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
				break;

			case 7:
				blockChainManager.pushBlock();
				break;

			case 8:
				blockChainManager.getMe().displayTransactions();
				break;

			case 9:
				blockChainManager.getMe().displayPendingTransaction();
				break;

			case 10:
				blockChainManager.makeBlockFromPendings();
				break;

			case 11:
				// 1
				displayStatus();
				System.err.println("Adding Genesis");
				blockChainManager.makeGenesis();
				displayStatus();

				// 2
				System.err.println("Adding transaction CREATE");
				DateFormat d = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Calendar c = Calendar.getInstance();
				Date t = c.getTime();        
				String dateB = d.format(t);
				c.add(Calendar.DAY_OF_MONTH, 7);
				String dateES = d.format(t);
				c.add(Calendar.DAY_OF_MONTH, 7);
				String dateE = d.format(t);
				// Make payload from data
				PayloadCreation payloadT = new PayloadCreation("TestNom","TestDescription","TestLieu", dateB, dateES, dateE, 1 , 10);
				// Make transaction from payload
				Transaction transactionT = new Transaction(blockChainManager.getMe().getPublicKey(), blockChainManager.getMe().getPrivateKey(), payloadT, blockChainManager.getNextId(),TransactionTypeEnum.CREATION);
				// Add transaction to list
				blockChainManager.addPendingTransaction(transactionT);
				displayStatus();

				// 3
				System.err.println("Adding transaction REGISTER");
				// Make payload from data
				PayloadRegister payloadTT = new PayloadRegister("EventHash");
				// Make transaction from payload
				Transaction transactionTT = new Transaction(blockChainManager.getMe().getPublicKey(), blockChainManager.getMe().getPrivateKey(), payloadTT, blockChainManager.getNextId(), TransactionTypeEnum.REGISTER); 
				// Add transaction to list
				blockChainManager.addPendingTransaction(transactionTT);
				displayStatus();

				// 4
				System.err.println("Adding transactions to a block");
				blockChainManager.makeBlockFromPendings();
				displayStatus();

				// 5
				System.err.println("Pushing Block to BlockChain");
				blockChainManager.pushBlock();
				displayStatus();

				// 5

				System.err.println("BlockChain is valid : " + blockChainManager.isChainValid());

				
				JSONObject jsonO = JsonUtils.makeJson(transactionTT.getPublicKey(), null , ((PayloadRegister) transactionTT.getPayload()).getEventHash());
				try {
					System.out.println(jsonO.toString(3));
					Transaction ttt = JsonUtils.transactionFromJson(jsonO.toString());
					blockChainManager.addPendingTransaction(transactionTT);
					displayStatus();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;


			default:
				break;
			}



		}while(run);

		sc.close();
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
		System.out.println(" 8. Display node transactions ");
		System.out.println(" 9. Display pending transactions ");
		System.out.println(" 10.Make block from pending transactions ");
		System.out.println(" 11.Run full test ");
		System.out.println(" ================================================== \n");
	}

	public static void displayStatus() {
		System.err.println("BlockChain Size : " + blockChainManager.getBlockChain().getSize() + " | Pending Blocks : " + blockChainManager.getNbPendingBlocks() + " | Pending transactions : " + blockChainManager.getNbPendingTransactions() +" | Effective Transactions : "+ blockChainManager.getNbTransactions());
	}

}

package server;

import chain.BlockChainManager;
import chain.Transaction;
import chain.TransactionTypeEnum;
import network.JsonUtils;
import network.PayloadCreation;
import network.PayloadRegister;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DemoClient {
    public static void main(String[] args) {
        Socket s;
        PrintWriter writter = null;
        BlockChainManager blockChainManager = new BlockChainManager();
        try {
            s = new Socket("127.0.0.1", 7777);
            writter = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
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
        PayloadCreation payloadT = new PayloadCreation("TestNom","TestDescription",
                "TestLieu", dateB, dateES, dateE, 1 , 10);
        // Make transaction from payload
        Transaction transactionT = new Transaction(blockChainManager.getMe().getPublicKey(),
                blockChainManager.getMe().getPrivateKey(), payloadT, blockChainManager.getNextId(),
                TransactionTypeEnum.CREATION);
        JSONObject jsonO = JsonUtils.makeJson(transactionT.getPublicKey(), transactionT.payload , "");
        writter.write("ID/99/\n");
        writter.write("NEW_TRANSACTION/" + jsonO.toString() + "/\n");
        writter.flush();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

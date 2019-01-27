package server;

import server.protocol.GetBlock;
import server.protocol.Request;
import server.protocol.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientConnection;
    private PrintWriter clientWritter;
    private RequestHandler chain;

    public ClientHandler(RequestHandler chain, Socket clientConnection) {
        this.chain = chain;
        this.clientConnection = clientConnection;
    }

    @Override
    public void run() {
        try {
            clientWritter = new PrintWriter(clientConnection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
            while (clientConnection.isConnected()) {
                Request request = RequestParser.parserRequest(in.readLine());
                Object response = null;

                if (request instanceof GetBlock) {
                    chain.onGetBlock((GetBlock) request);
                }

                if (response != null) {
                    // TODO: send response
                }
            }
        } catch (IOException e) {
            System.err.println("Client connection error: " +  e.getMessage());
            interrupt();
        }
    }

    /**
     * Send a 'DISCONNECT' message to the client and kill the client's thread.
     */
    public void disconnect() {
        if (clientWritter != null) {
            clientWritter.println("DISCONNECTED/");
        }
        interrupt();
    }
}

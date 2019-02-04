package server;

import chain.Block;
import server.protocol.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PeerConnection extends Thread {
    private int id;
    private Socket socket;
    private IServer server;
    private PrintWriter clientWritter;
    private BufferedReader socketReader;

    public PeerConnection(IServer server, int id, Socket clientSocket) {
        this(server, id, clientSocket, null);
    }

    public PeerConnection(IServer server, int id, Socket clientSocket, BufferedReader socketReader) {
        this.server = server;
        this.socket = clientSocket;
        this.socketReader = socketReader;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            clientWritter = new PrintWriter(socket.getOutputStream(), true);
            if (socketReader == null) {
                socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            while (socket.isConnected()) {
                Request request = RequestParser.parserRequest(socketReader.readLine());
                Object response = null;

                if (request instanceof BlockRequest) {
                    Block b = ((BlockRequest) request).getBlock();
                    server.getChain().addBlock(b, id);
                } else if (request instanceof GetBlocks) {
                    response = server.getChain().getBlocs((GetBlocks) request);
                } else if (request instanceof NewTransaction) {
                    server.getChain().newTransaction((NewTransaction) request);
                }

                if (response != null) {
                    clientWritter.write(response.toString() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Client connection error: " +  e.getMessage());
            interrupt();
        }
    }

    /**
     * Sends the given request to the peer.
     * Returns false if the peer can't be reached, true otherwise.
     */
    public boolean send(Request r) {
        clientWritter.write(r.toString() + "\n");
        return clientWritter.checkError();
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

    public Socket getSocket() {
        return socket;
    }
}

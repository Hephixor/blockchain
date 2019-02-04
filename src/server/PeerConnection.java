package server;

import chain.Block;
import server.protocol.BlockRequest;
import server.protocol.GetBlock;
import server.protocol.Request;
import server.protocol.RequestParser;

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

                if (request instanceof GetBlock) {
                    response = server.getChain().getBlock((GetBlock) request);
                } else if (request instanceof BlockRequest) {
                    Block b = ((BlockRequest) request).getBlock();
                    server.getChain().blockReceived(b, id);
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
     */
    public void send(Request r) {
        clientWritter.write(r.toString() + "\n");
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

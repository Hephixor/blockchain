package server;

import chain.BlockChain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
	public static final int DEFAULT_PORT = 7777;

	private ServerSocket serverSocket;
	private Thread executionThread;
	private List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
	private ServerBlockChain blockchain;

	/**
	 * Create and initialize a concurrent node server that will listen on the
	 * DEFAULT_PORT port.
	 */
	public Server(ServerBlockChain chain) throws IOException {
		this(chain, DEFAULT_PORT);
	}

	/**
	 * Create and initialize a concurrent node server.
	 * @param port: the port to listen to
	 */
	public Server(ServerBlockChain chain, int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.blockchain = chain;
		start();
	}

	/**
	 * Start the server's thread.
	 */
	private void start() {
		Runnable task = () -> {
			try {
				acceptConnection();
			} catch (IOException e) {
				System.err.println("IO Error: " + e.getMessage());
				stop();
			}
		};
		executionThread = new Thread(task);
		executionThread.start();
	}

	/**
	 *  Accept a client connection and start a thread to process the client's request.
	 * @throws IOException
	 */
	private void acceptConnection() throws IOException {
		Socket clientSocket = serverSocket.accept();
		ClientHandler newHandler = new ClientHandler(blockchain, clientSocket);
		clientHandlers.add(newHandler);
		newHandler.start();
	}

	/**
	 * Disconnect all the clients and kill the server's thread.
	 */
	public void stop() {
		clientHandlers.forEach(ClientHandler::disconnect);
		if (executionThread != null) {
			executionThread.interrupt();
		}
	}
}

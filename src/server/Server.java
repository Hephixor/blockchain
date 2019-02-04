package server;

import server.protocol.Id;
import server.protocol.Request;
import server.protocol.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

public class Server implements IServer {
	static final int DEFAULT_PORT = 7777;
	static final int DEFAULT_ID = 0;

	private ServerSocket serverSocket;
	private Thread executionThread;
	private ConcurrentBlockChain blockchain;
	private ConsensusManager consensusManager;
	private PeersManager peersManager;
	private int id;

	/**
	 * Create and initialize a concurrent node server that will listen on the
	 */
	public Server(List<IpAddress> allowedAdresses, ConcurrentBlockChain chain) throws IOException {
		this(DEFAULT_PORT, DEFAULT_ID, allowedAdresses, chain);
	}

	/**
	 * Create and initialize a concurrent node server.
	 */
	public Server(int port, int id, List<IpAddress> allowedAdresses, ConcurrentBlockChain chain) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.blockchain = chain;
		this.blockchain.setServer(this);
		this.id = id;
		consensusManager = new ConsensusManager(this);
		peersManager = new PeersManager(this, allowedAdresses);
	}

	/**
	 * Start the server's thread.
	 */
	public Thread start() {
	    new Thread(consensusManager).start();
	    System.out.println("Serving on port: " + serverSocket.getLocalPort() + " with id " + id);
        peersManager.connectToPeers();
		Runnable task = () -> {
			try {
				listen();
			} catch (IOException e) {
				System.err.println("IO Error: " + e.getMessage());
				stop();
			}
		};
		executionThread = new Thread(task);
		executionThread.start();
		return executionThread;
	}

	/**
	 *  Accept incoming connections from peers.
	 * @throws IOException
	 */
	private void listen() throws IOException {
	    while(true) {
            Socket s = serverSocket.accept();
            peersManager.acceptConnection(s);
        }
	}

	/**
	 * Disconnect all the clients and kill the server's thread.
	 */
	public void stop() {
		//peerConnections.values().forEach(PeerConnection::disconnect);
		if (executionThread != null) {
			executionThread.interrupt();
		}
	}
	
	public int getDefaultPort() {
		return DEFAULT_PORT;
	}

    @Override
    public ConcurrentBlockChain getChain() {
        return blockchain;
    }

    @Override
    public ConsensusManager getConsensusManager() {
        return consensusManager;
    }

    @Override
    public PeersManager getPeersManager() {
        return peersManager;
    }

    @Override
    public int getNodeId() {
        return id;
    }
}

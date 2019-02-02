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
	private Map<Integer, PeerConnection> peerConnections = Collections.synchronizedMap(new HashMap<>());
	private ConcurrentBlockChain blockchain;
	private List<IpAddress> allowedAdresses;
	private ConsensusManager consensusManager;
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
		this.allowedAdresses = allowedAdresses;
		this.id = id;
		consensusManager = new ConsensusManager(this);
	}

	/**
	 * Start the server's thread.
	 */
	public Thread start() {
	    new Thread(consensusManager).start();
	    System.out.println("Serving on port: " + serverSocket.getLocalPort() + " with id " + id);
        connectToPeers();
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
     * Try to establish a connection with the allowed peers.
     */
    private void connectToPeers() {
        allowedAdresses.forEach(address -> {
            Socket s;
            try {
                s = new Socket(address.getAddress(), address.getPort());
                connect(s);
            } catch (IOException e) {
                System.err.println("Cannot join peer: " + address);
            }
        });
    }

	/**
	 *  Accept incoming connections from peers.
	 * @throws IOException
	 */
	private void listen() throws IOException {
	    while(true) {
            Socket s = serverSocket.accept();
            if (isAllowed(s.getLocalSocketAddress())) {
                System.out.println("Incoming connection from " + s.getRemoteSocketAddress());
                connect(s);
            } else {
                System.out.println("Connection attempt from invalid address: " + s.getRemoteSocketAddress().toString());
                s.close();
            }
        }
	}

    /**
     * Try to establish a connection over the given socket.
     */
	private void connect(Socket s) {
	    BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            System.out.println("Unable to read from peer: " + s.getRemoteSocketAddress() + "\n" + e.getMessage());
            return;
        }

        Executors.newSingleThreadExecutor().submit(() -> {
            Request request;
            try {
                s.getOutputStream().write(((new Id(id).toString()) + "\n").getBytes());
                request = RequestParser.parserRequest(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (request instanceof Id) {
                registerPeerConnection(s, reader, ((Id) request).id);
            }
        });
    }

    private void registerPeerConnection(Socket s, BufferedReader reader, Integer id) {
        if (peerConnections.get(id) != null || id == this.id) {
            System.err.println("Peer with id " + id + " is already connected");
        } else {
            PeerConnection handler = new PeerConnection(this, id, s, reader);
            handler.start();
            peerConnections.put(id, handler);
            System.out.println("Connected to peer " + id);
        }
    }

	private boolean isAllowed(SocketAddress address) {
        String[] components = stringOfSockAddress(address).split(":");
	    return components.length > 0
                && allowedAdresses.stream().anyMatch(a -> a.getAddress().equals(components[0]));
    }

    private static String stringOfSockAddress(SocketAddress address) {
	    return address.toString().trim().replace("/", "");
    }

	/**
	 * Disconnect all the clients and kill the server's thread.
	 */
	public void stop() {
		peerConnections.values().forEach(PeerConnection::disconnect);
		if (executionThread != null) {
			executionThread.interrupt();
		}
	}
	
	public int getDefaultPort() {
		return DEFAULT_PORT;
	}

    @Override
    public int getMaxPeersCount() {
        return allowedAdresses.size();
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
    public void setLeader(int id) {
        System.out.println("The leader is now " + id);
    }
}

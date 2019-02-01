package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server implements IServer {
	static final int DEFAULT_PORT = 7777;

	private ServerSocket serverSocket;
	private Thread executionThread;
	private List<PeerConnection> peerConnections = Collections.synchronizedList(new ArrayList<>());
	private ServerBlockChain blockchain;
	private List<IpAddress> allowedAdresses;

	/**
	 * Create and initialize a concurrent node server that will listen on the
	 * DEFAULT_PORT port.
	 */
	public Server(ServerBlockChain chain, List<IpAddress> allowedAdresses) throws IOException {
		this(chain, DEFAULT_PORT, allowedAdresses);
	}

	/**
	 * Create and initialize a concurrent node server.
	 * @param port: the port to listen to
	 */
	public Server(ServerBlockChain chain, int port, List<IpAddress> allowedAdresses) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.blockchain = chain;
		this.allowedAdresses = allowedAdresses;
	}

	/**
	 * Start the server's thread.
	 */
	public Thread start() {
	    System.out.println("Serving on port: " + serverSocket.getLocalPort());
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
            if (isSelf(address)) {
                return;
            }
            Socket s;
            try {
                s = new Socket(address.getAddress(), address.getPort());
                peerConnections.add(new PeerConnection(blockchain, s));
                System.out.println("Connected to peer: " + address);
            } catch (IOException e) {
                System.err.println("Cannot connect to peer: " + address);
            }
        });
    }

    private boolean isSelf(IpAddress otherAddress) {
        return otherAddress.getAddress().equals("127.0.0.1")
                && otherAddress.getPort() == serverSocket.getLocalPort();
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
                PeerConnection newHandler = new PeerConnection(blockchain, s);
                peerConnections.add(newHandler);
                newHandler.start();
            } else {
                System.out.println("Connection attempt from invalid address: " + s.getRemoteSocketAddress().toString());
                s.close();
            }
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
		peerConnections.forEach(PeerConnection::disconnect);
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
}

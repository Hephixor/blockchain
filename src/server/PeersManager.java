package server;

import server.protocol.Id;
import server.protocol.Request;
import server.protocol.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class PeersManager {
    private List<IpAddress> allowedAdresses;
    private Map<Integer, PeerConnection> peerConnections = Collections.synchronizedMap(new HashMap<>());
    private IServer server;

    public PeersManager(IServer server, List<IpAddress> allowedAdresses) {
        this.server = server;
        this.allowedAdresses = allowedAdresses;
    }

    /**
     * Try to establish a connection with the allowed peers.
     */
    public void connectToPeers() {
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

    public void acceptConnection(Socket s) throws IOException {
        if (isAllowed(s.getLocalSocketAddress())) {
            System.out.println("Incoming connection from " + s.getRemoteSocketAddress());
            connect(s);
        } else {
            System.out.println("Connection attempt from invalid address: " + s.getRemoteSocketAddress().toString());
            s.close();
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
                s.getOutputStream().write(((new Id(server.getNodeId()).toString()) + "\n").getBytes());
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
        if (peerConnections.get(id) != null || id == server.getNodeId()) {
            System.err.println("Peer with id " + id + " is already connected");
        } else {
            PeerConnection handler = new PeerConnection(server, id, s, reader);
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

    public int getMaxPeersCount() {
        return allowedAdresses.size();
    }

    public void send(int targetId, Request r) {
        if (peerConnections.get(targetId) == null) {
            return;
        }
        PeerConnection target = peerConnections.get(targetId);
        if (! target.send(r)) {
            peerConnections.remove(targetId);
        }
    }
}

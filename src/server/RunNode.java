package server;

import chain.BlockChain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RunNode {
    public static final String DEFAULT_PEERS_FILE = "peers";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("missing arguments");
            System.exit(1);
        }

        String peersFileName = DEFAULT_PEERS_FILE;
        int port = Integer.valueOf(args[0]);
        int id = Integer.valueOf(args[1]);

        if (args.length == 3) {
            peersFileName = args[2];
        }

        List<IpAddress> allowedPeers = readAllowedPeers(peersFileName);
        ConcurrentBlockChain blockchain = new ConcurrentBlockChain(new BlockChain());
        Server server = null;

        try {
            server = new Server(port, id, allowedPeers, blockchain);
        } catch (IOException e) {
            System.err.println("Error while initializing the server: " + e.getMessage());
            System.exit(1);
        }

        try {
            server.start().join();
        } catch (InterruptedException e) {
            server.stop();
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<IpAddress> readAllowedPeers(String peersFilename) {
        try {
            return Files.lines(Paths.get(peersFilename))
                    .map(IpAddress::fromString)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Cannot open peers file");
            System.exit(1);
        }
        return null;
    }
}

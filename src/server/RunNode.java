package server;

import chain.BlockChain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RunNode {
    public static final String DEFAULT_PEERS_FILE = "peers";

    public static void main(String[] args) {
        String peersFileName = DEFAULT_PEERS_FILE;
        int port = Server.DEFAULT_PORT;

        if (args.length == 1 && args[0].matches("\\d+")) {
            port = Integer.valueOf(args[0]);
        } else if (args.length == 1 && !args[0].matches("\\d+")) {
            peersFileName = args[0];
        }

        if (args.length >= 2) {
            peersFileName = args[1];
        }

        List<IpAddress> allowedPeers = readAllowedPeers(peersFileName);
        ServerBlockChain blockchain = new ServerBlockChain(new BlockChain());
        Server server = null;

        try {
            server = new Server(blockchain, port, allowedPeers);
        } catch (IOException e) {
            System.err.println("Error while initializing the server: " + e.getMessage());
            System.exit(1);
        }

        try {
            server.start().join();
        } catch (InterruptedException e) {
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

package server;

public interface IServer {
    /**
     *  A Reference to the server's blockchain
     */
    ConcurrentBlockChain getChain();

    /**
     *  A reference to the sever's consensus manager.
     */
    ConsensusManager getConsensusManager();

    /**
     * A reference to the server's peers manager.
     */
    PeersManager getPeersManager();

    /**
     * Returns the id of the current node.
     */
    int getNodeId();
}

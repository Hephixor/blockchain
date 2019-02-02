package server;

public interface IServer {
    /**
     * Get the number of allowed addresses.
     */
    int getMaxPeersCount();

    /**
     *  A Reference to the current server's blockchain
     */
    ConcurrentBlockChain getChain();

    /**
     * Set the id of the node that is currently allowed to write a block.
     */
    void setLeader(int id);
}

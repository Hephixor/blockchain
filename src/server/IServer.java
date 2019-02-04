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
     *  A reference to the current sever's consensus manager.
     */
    ConsensusManager getConsensusManager();

    /**
     * Set the id of the node that is currently allowed to write a block.
     */
    void setLeader(int id);
}

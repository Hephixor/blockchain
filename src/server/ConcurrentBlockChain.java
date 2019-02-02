package server;

import chain.Block;
import chain.BlockChain;
import server.protocol.BlockRequest;
import server.protocol.GetBlock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentBlockChain {
    private BlockChain blockChain;
    private ReadWriteLock blockChainLock = new ReentrantReadWriteLock();
    private IServer server;

    public ConcurrentBlockChain(BlockChain blockChain) {
        this.blockChain = blockChain;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public BlockRequest getBlock(GetBlock getBlock) {
        BlockRequest result = null;
        blockChainLock.readLock().lock();
        Block b = blockChain.getBlockAtIndex(getBlock.getBlockNumber());
        if (b != null) {
            result = new BlockRequest(b);
        }
        blockChainLock.readLock().unlock();
        return result;
    }

    public void blockReceived(Block block, int nodeId) {
        int expectedId = server.getConsensusManager().leaderAtTime(block.getTimeStamp());
        if (expectedId != nodeId) {
            System.err.println("Attempt to create a block from a non leader node: " + nodeId);
            return;
        }
        blockChainLock.writeLock().lock();
        blockChain.addBlock(block);
        blockChainLock.writeLock().unlock();
    }
}

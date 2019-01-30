package server;

import chain.BlockChain;
import server.protocol.GetBlock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class ServerBlockChain implements RequestHandler{
    private BlockChain blockChain;
    private ReadWriteLock blockChainLock = new ReentrantReadWriteLock();

    public ServerBlockChain(BlockChain blockChain) {
        this.blockChain = blockChain;
    }

    @Override
    public Object onGetBlock(GetBlock getBlock) {
        blockChainLock.readLock().lock();
        // TODO: lire le block getBlock.blockNumber de blockChain
        blockChainLock.readLock().unlock();
        return null;
    }
}

package server;

import chain.Block;
import chain.BlockChain;
import chain.BlockChainManager;
import server.protocol.BlockRequest;
import server.protocol.Blocks;
import server.protocol.GetBlocks;
import server.protocol.NewTransaction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentBlockChain {
    private BlockChainManager blockChainManager;
    private ReadWriteLock blockChainLock = new ReentrantReadWriteLock();
    private IServer server;

    public ConcurrentBlockChain(BlockChainManager blockChainManager) {
        this.blockChainManager = blockChainManager;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public void newTransaction(NewTransaction transactionRequest) {
        blockChainLock.writeLock().lock();
        blockChainManager.addPendingTransaction(transactionRequest.getTransaction());
        blockChainLock.writeLock().unlock();
    }

    public Blocks getBlocs(GetBlocks getBlocksRequest) {
        ArrayList<Block> blocks = new ArrayList<>();
        for (int i = getBlocksRequest.getMinBlockNumber() + 1; i < blockChainManager.getBlockChain().getSize(); i++) {
            blocks.add(blockChainManager.getBlockChain().getBlockAtIndex(i));
        }
        return new Blocks(blocks);
    }

    public void pushBendingBlock() {
        blockChainLock.writeLock().lock();
        blockChainManager.makeBlockFromPendings();
        Block lastBlock = null;
        if (blockChainManager.getMe().pending()) {
            blockChainManager.pushBlock();
            int lastIndex = blockChainManager.getBlockChain().getSize() - 1;
            lastBlock = blockChainManager.getBlockChain().getBlockAtIndex(lastIndex);
            System.out.println("Pushed bloc to the blochain:\n" + lastBlock.toString());
        }
        blockChainLock.writeLock().unlock();
        if (lastBlock != null) {
            server.getPeersManager().broadcast(new BlockRequest(lastBlock));
        }
    }

    public boolean addBlock(Block block, int emitterId) {
        boolean added = false;
        System.out.println("Received block:\n" + block);
        blockChainLock.writeLock().lock();
        if (checkBlock(block, emitterId)) {
            blockChainManager.addBlockToBlockChain(block);
            added = true;
        } else {
            System.err.println("Refused invalid block:\n" + block.toString());
        }
        blockChainLock.writeLock().unlock();
        return added;
    }

    public boolean checkBlock(Block block, int emitterId) {
        long timeDiff = Math.abs(Instant.now().toEpochMilli() - block.getTimeStamp());
        if (timeDiff > ConsensusManager.INTERVAL_IN_MILLIS * 2) {
            System.err.println("New block's timestamp is too far from now !");
            return false;
        }

        int expectedId = server.getConsensusManager().leaderAtTime(block.getTimeStamp());
        if (emitterId != server.getConsensusManager().getCurrentLeader() && expectedId != emitterId) {
            System.err.println("Attempt to create a block from a non leader node: " + emitterId);
            return false;
        }

        blockChainLock.writeLock().lock();
        blockChainManager.getBlockChain().addBlock(block);
        boolean valid = blockChainManager.isChainValid();
        blockChainManager.getBlockChain().removeBlock(blockChainManager.getBlockChain().getSize() - 1);
        blockChainLock.writeLock().unlock();

        return valid;
    }

    public boolean checkBlocs(List<Block> blocks, int emitterId) {
        blockChainLock.writeLock().lock();
        int removeFrom = blockChainManager.getBlockChain().getSize();
        int currentBlock = 0;


        while (currentBlock < blockChainManager.getBlockChain().getSize() - 1 && addBlock(blocks.get(currentBlock), emitterId)) {
            currentBlock++;
        }
        blockChainManager.getBlockChain().removeBlocksFromIndex(removeFrom);
        blockChainLock.writeLock().unlock();

        return currentBlock == blocks.size();
    }
}

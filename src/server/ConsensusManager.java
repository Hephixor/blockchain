package server;

import merkle.Hash;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsensusManager implements Runnable {
    public static final int INTERVAL_IN_MILLIS = 5000;

    private IServer server;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ConsensusManager(IServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        Runnable task = () -> {
            int currentLeader = computeCurrentLeader();
            System.out.println("The leader is now " + currentLeader);
            if (currentLeader == server.getNodeId()) {
                server.getChain().pushBendingBlocks();
            }
        };
        scheduler.scheduleAtFixedRate(task, 0, INTERVAL_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns the id of the current leader.
     */
    private int computeCurrentLeader() {
        return leaderAtNthInterval(currentIntervalNumber());
    }

    /**
     * Returns the number of intervals since epoch.
     */
    private int currentIntervalNumber() {
        return (int) (Instant.now().toEpochMilli() / INTERVAL_IN_MILLIS);
    }

    /**
     * Returns the index of the leader at instant t.
     */
    public int leaderAtTime(long time) {
        return leaderAtNthInterval((int) time / INTERVAL_IN_MILLIS);
    }

    /**
     * Returns the index of the leader at the nth interval since epoch.
     */
    private int leaderAtNthInterval(int s) {
        byte[] data = Hash.digestSHA256String(Integer.toString(s));
        int hashInt = ByteBuffer.wrap(data).getInt();
        return Math.abs(hashInt % server.getPeersManager().getMaxPeersCount());
    }
}

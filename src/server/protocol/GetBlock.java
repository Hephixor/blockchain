package server.protocol;

public class GetBlock extends Request {
    private int blockNumber;

    public GetBlock(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getBlockNumber() {
        return blockNumber;
    }
}

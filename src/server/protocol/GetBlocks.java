package server.protocol;

public class GetBlocks extends Request {
    private int minBlockNumber;

    public GetBlocks(int minBlockNumber) {
        this.minBlockNumber = minBlockNumber;
    }

    public int getMinBlockNumber() {
        return minBlockNumber;
    }

    @Override
    public String toString() {
        return String.format("GET_BLOCKS/%d/", minBlockNumber);
    }
}

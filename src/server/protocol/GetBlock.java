package server.protocol;

public class GetBlock extends Request {
    public int blockNumber;

    public GetBlock(int blockNumber) {
        this.blockNumber = blockNumber;
    }
}

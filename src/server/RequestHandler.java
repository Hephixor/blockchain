package server;

import server.protocol.GetBlock;

public interface RequestHandler {
    /**
     * Returns the nth block of the current chain.
     * @param getBlock: the GET_BLOCK request data
     * @return
     */
    Object onGetBlock(GetBlock getBlock);
}

package server.protocol;

import chain.Block;

import java.util.List;

public class Blocks extends Request {
    private List<Block> blocks;

    public Blocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        StringBuilder blocksStr = new StringBuilder();
        blocksStr.append("{\"blocks\":[");
        for (int i = 0; i < blocks.size(); i++) {
            blocksStr.append(BlockRequest.stringOfBlock(blocks.get(i)));
            if (i < blocks.size() - 1) {
                blocksStr.append(',');
            }
        }
        blocksStr.append("]}");
        return String.format("BLOCKS/%s/", blocksStr);
    }
}

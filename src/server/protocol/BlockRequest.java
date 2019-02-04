package server.protocol;

import chain.Block;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;

public class BlockRequest extends Request {
    private Block block;

    public static BlockRequest fromJSONString (String json) {
        Block b = blockOfJSON(json);
        return b == null ? null : new BlockRequest(b);
    }

    public BlockRequest(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public static Block blockOfJSON(String blockStr) {
        if (blockStr.isEmpty()) {
            return null;
        }
        JSONObject jsonBlock;
        try {
            jsonBlock = new JSONObject(blockStr);
            Integer level = jsonBlock.getInt("level");
            String hash = jsonBlock.getString("hash");
            String previous_hash = jsonBlock.getString("previous_hash");
            Instant timestamp = Instant.parse(jsonBlock.getString("timestamp"));
            String merkleRoot = jsonBlock.getString("merkle_root");
            return new Block(level, hash, previous_hash, timestamp.toEpochMilli(), merkleRoot);
        } catch (JSONException e) {
            System.err.println("Error while parsing JSON block: " + e.getMessage());
            return null;
        }
    }

    public static String stringOfBlock(Block b) {
        String formatString =
                "{\"level\":%d,\"hash\":\"%s\",\"previous_hash\":\"%s\",\"timestamp\":%s,\"merkleRoot\":\"%s\"}";
        Instant timeStamp = Instant.ofEpochMilli(b.getTimeStamp());
        return String.format(formatString, b.getLevel(), b.getHash(),
                b.getPreviousHash(), timeStamp.toString(), b.getMerkleRoot());
    }

    @Override
    public String toString() {
        return String.format("BLOCK/%s/", stringOfBlock(block));
    }
}

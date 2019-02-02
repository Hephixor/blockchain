package server.protocol;

import chain.Block;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
    // Get block number n for the network.
    static final Pattern GET_BLOCK = Pattern.compile("GET_BLOCK/(\\d+)/");

    // Ask the network for blocks which number is above n.
    static final Pattern GET_BLOCKS = Pattern.compile("GET_BLOCKS/(\\d+)/");

    // Broadcast block number n to the network.
    static final Pattern BLOCK = Pattern.compile("BLOCK/(.*)/");

    // BroadCast a part of the chain to the network.
    static final Pattern BLOCKS = Pattern.compile("BLOCKS/(.*)/");

    // Broadcast this node's id to the network.
    static final Pattern ID = Pattern.compile("ID/(\\d+)/");

    /**
     * Parse a client request. Returns null if the request is invalid.
     */
    public static Request parserRequest(String request) {
        if (request == null || request.isEmpty()) {
            return null;
        }

        if (request.matches(GET_BLOCK.pattern())) {
            return parseGetBlock(request);
        } else if (request.matches(GET_BLOCKS.pattern())) {
            return parseGetBlocks(request);
        } else if (request.matches(BLOCKS.pattern())) {
            return parseBlocks(request);
        } else if (request.matches(BLOCK.pattern())) {
            return parseBlock(request);
        } else if (request.matches(ID.pattern())) {
            return parseId(request);
        }

        return null;
    }

    /**
     * Parse a GET_BLOCK request. Returns null in case of  invalid request.
     * ex: GET_BLOCK/1/
     */
    private static GetBlock parseGetBlock(String request) {
        Matcher m = GET_BLOCK.matcher(request);
        m.find();
        Integer blockNumber = Integer.valueOf(m.group(1));
        return new GetBlock(blockNumber);
    }

    /**
     * Parse a GET_BLOCKs request. Returns null in case of  invalid request.
     * ex: GET_BLOCK/0/
     */
    private static Request parseGetBlocks(String request) {
        Matcher m = GET_BLOCKS.matcher(request);
        m.find();
        Integer minBlockNumber = Integer.valueOf(m.group(1));
        return new GetBlocks(minBlockNumber);
    }

    /**
     * Parse a BLOCK request. Returns null in case of  invalid request.
     * ex: BLOCK/{"blocks":[<bloc1>,<bloc2>,...]}/
     */
    private static Request parseBlocks(String request) {
        Matcher m = BLOCKS.matcher(request);
        m.find();
        try {
            ArrayList<Block> blocks = new ArrayList<>();
            JSONArray jsonBlocks = new JSONObject(request).getJSONArray("blocks");
            for (int i = 0; i < jsonBlocks.length(); i++) {
                blocks.add(BlockRequest.blockOfJSON(jsonBlocks.getJSONObject(i).toString()));
            }
            return new Blocks(blocks);
        } catch (JSONException e) {
            System.err.println("Cannot parse JSON blocks: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parse a BLOCK request. Returns null in case of  invalid request.
     * ex: BLOCK/{"level":1,"hash":"0x45fe","previous_hash":"0xfffff","timestamp":"2010-01-01T12:00:00Z","merkel_root":"0xfffff"}/
     */
    private static BlockRequest parseBlock(String request) {
        Matcher m = BLOCK.matcher(request);
        m.find();
        return BlockRequest.fromJSONString(m.group(1));
    }

    /**
     * Parse an ID request. Returns null in case of invalid request.
     * ex: ID/1/
     */
    private static Request parseId(String request) {
        Matcher m = ID.matcher(request);
        m.find();
        Integer id = Integer.valueOf(m.group(1));
        return new Id(id);
    }
}

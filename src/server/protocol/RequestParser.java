package server.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
    static final Pattern GET_BLOCK = Pattern.compile("GET_BLOCK/(\\d+)/");
    static final Pattern ID = Pattern.compile("ID/(\\d+)/");

    /**
     * Parse a client request. Returns null if the request is invalid.
     * See protocol.md
     * @param request: the request to parse.
     * @return
     */
    public static Request parserRequest(String request) {
        if (request == null || request.isEmpty()) {
            return null;
        }

        if (request.matches(GET_BLOCK.pattern())) {
            return parseGetBlock(request);
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

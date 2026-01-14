package webserver.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.RequestParsingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static webserver.constant.HttpHeader.CONTENT_LENGTH;
import static webserver.constant.HttpHeader.CONTENT_TYPE;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    private static final String FORM_URLENCODED = "application/x-www-form-urlencoded";
    private String path;
    private String method;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    public RequestParser(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        parseRequestLine(br);
        parseHeader(br);
        parseBody(in);
    }

    private void parseHeader(BufferedReader br) throws IOException {
        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            logger.debug("header line : {}", headerLine);
            int idx = headerLine.indexOf(':');
            if (idx > 0) {
                String field = headerLine.substring(0, idx).trim();
                String value = headerLine.substring(idx + 1).trim();
                headers.put(field, value);
            }
        }
        logger.debug("headers.size : {}", this.headers.size());
    }

    private void parseRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        logger.debug("request line {}", requestLine);

        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            throw new RequestParsingException();
        }

        this.method = tokens[0];
        logger.debug("method : {}", this.method);

        String url = tokens[1];
        tokens = url.split("\\?", 2);

        this.path = tokens[0];
        logger.debug("path : {}", this.path);

        if (tokens.length == 2) {
            parseParameters(tokens[1]);
            logger.debug("params.size : {}", this.params.size());
        }
    }

    private void parseParameters(String rawQueryString) {
        String[] rawQueryParams = rawQueryString.split("&");
        for (String rawQueryParam : rawQueryParams) {
            String[] tokens = rawQueryParam.split("=");
            if (tokens.length == 2) {
                String key = tokens[0].trim();
                String value = tokens[1].trim();
                this.params.put(key, value);
            }
        }
    }

    private void parseBody(InputStream in) throws IOException {
        if (!canParseBody()) {
            return;
        }

        if (isFormUrlEncoded()) {
            parseUrlEncodedBody(in);
        }
    }

    private boolean canParseBody() {
        return headers.containsKey(CONTENT_LENGTH.getHeader()) && headers.containsKey(CONTENT_TYPE.getHeader());
    }

    private boolean isFormUrlEncoded() {
        return headers.get(CONTENT_TYPE.getHeader()).equals(FORM_URLENCODED);
    }

    private void parseUrlEncodedBody(InputStream in) throws IOException {
        int contentLength = Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH.getHeader(), "0"));
        if (contentLength == 0) {
            return;
        }

        byte[] bodyBytes = in.readNBytes(contentLength);
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        logger.debug("body = {}", body);

        String[] inputs = body.split("&");
        for (String input : inputs) {
            String[] nameAndValue = input.split("=");
            String name = nameAndValue[0].trim();
            String value = nameAndValue[1].trim();
            this.params.put(name, value);
        }
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }
}

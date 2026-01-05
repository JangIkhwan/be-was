package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RequestGenerator.class);
    private String CONTENT_LENGTH = "Content-Length";
    private String CONTENT_TYPE = "Content-Type";
    private String FORM_URLENCODED = "application/x-www-form-urlencoded";
    private String path;
    private String method;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    public RequestGenerator(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String statusLine = br.readLine();
        logger.debug("status line {}", statusLine);

        parseStatusLine(statusLine);
        parseHeader(br);
        parseBody(br);
    }

    private void parseHeader(BufferedReader br) throws IOException {
        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            String[] tokens = headerLine.split(":");
            if(tokens.length == 2){
                String field = tokens[0].trim();
                String value = tokens[1].trim();
                this.headers.put(field, value);
            }
        }
        logger.debug("headers.size {}", this.headers.size());
    }

    private void parseStatusLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        if(tokens.length != 3){
            throw new IllegalArgumentException();
        }

        this.method = tokens[0];
        logger.debug("method {}", this.method);

        String url = tokens[1];
        tokens = url.split("\\?", 2);

        this.path = tokens[0];
        logger.debug("path = {}", this.path);

        if(tokens.length == 2){
            parseParameters(tokens[1]);
            logger.debug("params.size {}", this.params.size());
        }
    }

    private void parseParameters(String rawQueryString) {
        String[] rawQueryParams = rawQueryString.split("&");
        for(String rawQueryParam : rawQueryParams){
            String[] tokens = rawQueryParam.split("=");
            if(tokens.length == 2){
                String key = tokens[0].trim();
                String value = tokens[1].trim();
                this.params.put(key, value);
            }
        }
    }

    private void parseBody(BufferedReader br) throws IOException {
        if(!canParseBody()){
            return;
        }

        if(headers.get(CONTENT_TYPE).equals(FORM_URLENCODED)){
            parseUrlEncodedBody(br);
        }
    }

    private void parseUrlEncodedBody(BufferedReader br) throws IOException {
        int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH));
        char[] buffer = new char[contentLength];
        br.read(buffer, 0, contentLength);
        String body = new String(buffer);
        logger.debug("body = {}", body);

        String[] inputs = body.split("&");
        for(String input : inputs){
            String[] nameAndValue = input.split("=");
            String name = nameAndValue[0].trim();
            String value = nameAndValue[1].trim();
            this.params.put(name, value);
        }
    }

    private boolean canParseBody() {
        return headers.containsKey(CONTENT_LENGTH) && headers.containsKey(CONTENT_TYPE);
    }

    public String getPath() {
        return path;
    }

    public Request generate(){
        return new Request(method, path, params);
    }
}

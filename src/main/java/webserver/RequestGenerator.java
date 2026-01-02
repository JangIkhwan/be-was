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

    private String path;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    public RequestGenerator(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String requestLine = br.readLine();
        logger.debug("http first line {}", requestLine);

        parseRequestURL(requestLine);
        parseHeader(br);
    }

    private void parseHeader(BufferedReader br) throws IOException {
        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            String[] tokens = headerLine.split(": ");
            if(tokens.length == 2){
               this.headers.put(tokens[0], tokens[1]);
            }
        }
        logger.debug("headers.size {}", this.headers.size());
    }

    private void parseRequestURL(String requestLine) {
        String url = requestLine.split(" ")[1];
        String[] tokens = url.split("\\?", 2);

        this.path = tokens[0];
        logger.debug("path {}", this.path);

        if(tokens.length >= 2){
            parseParameters(tokens[1]);
        }
        logger.debug("params.size {}", this.params.size());
    }

    private void parseParameters(String rawQueryString) {
        String[] rawQueryParams = rawQueryString.split("&");
        for(String rawQueryParam : rawQueryParams){
            String[] tokens = rawQueryParam.split("=");
            if(tokens.length == 2){
                String key = tokens[0];
                String value = tokens[1];
                this.params.put(key, value);
            }
        }
    }

    public String getPath() {
        return path;
    }

    public Request generate(){
        return new Request(path, params);
    }
}

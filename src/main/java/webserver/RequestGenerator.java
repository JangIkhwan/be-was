package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RequestGenerator.class);

    private String path;
    private Map<String, String> params;

    public RequestGenerator(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String requestLine = br.readLine();
        List<String> headers = new ArrayList<>();
        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            headers.add(headerLine);
        }

        logger.debug("http first line {}", requestLine);

        String url = requestLine.split(" ")[1];
        String[] tokens = url.split("\\?", 2);
        this.path = tokens[0];

        logger.debug("path {}", this.path);

        this.params = new HashMap<>();
        if(tokens != null && tokens.length >= 2){
            parseParamters(tokens[1]);
        }
    }

    private void parseParamters(String rawQueryString) {
        logger.debug("rawQueryString {}", rawQueryString);
        String[] rawQueryParams = rawQueryString.split("&");
        for(String rawQueryParam : rawQueryParams){
            String[] keyAndValue = rawQueryParam.split("=");
            if(keyAndValue != null && keyAndValue.length == 2){
                String key = keyAndValue[0];
                String value = keyAndValue[1];
                params.put(key, value);
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

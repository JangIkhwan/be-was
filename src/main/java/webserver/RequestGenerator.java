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
    private Map<String, String> params;

    public RequestGenerator(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        logger.debug("http first line {}", line);

        String url = line.split(" ")[1];
        String[] tokens = url.split("\\?");
        this.path = tokens[0];

        logger.debug("path {}", this.path);

        this.params = new HashMap<>();
        parseParamters(tokens[1]);
    }

    private void parseParamters(String rawQueryString) {
        logger.debug("rawQueryString {}", rawQueryString);
        String[] rawQueryParams = rawQueryString.split("&");
        for(String rawQueryParam : rawQueryParams){
            String[] keyAndValue = rawQueryParam.split("=");
            String key = keyAndValue[0];
            String value = keyAndValue[1];
            params.put(key, value);
        }
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getPath() {
        return path;
    }

    public Request generate(){
        return new Request(path, params);
    }
}

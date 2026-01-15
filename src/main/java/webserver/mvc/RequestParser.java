package webserver.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.RequestParsingException;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static webserver.constant.HttpHeader.CONTENT_LENGTH;
import static webserver.constant.HttpHeader.CONTENT_TYPE;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    private static final String FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private String path;
    private String method;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private List<MultipartFile> multipartFiles = new ArrayList<>();
    private MultipartRequestParser multipartParser = new MultipartRequestParser();

    public RequestParser(InputStream in) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(in);

        byte[] headerBytes = readUntilHeaderEnd(bin);
        parseRequestLineAndHeaders(headerBytes);

        byte[] bodyBytes = readBody(bin);

        parseBody(bodyBytes);
    }

    private byte[] readUntilHeaderEnd(BufferedInputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int prev = -1, curr;
        while ((curr = in.read()) != -1) {
            buffer.write(curr);

            if (prev == '\r' && curr == '\n') {
                byte[] bytes = buffer.toByteArray();
                int len = bytes.length;
                if (len >= 4 &&
                        bytes[len - 4] == '\r' &&
                        bytes[len - 3] == '\n' &&
                        bytes[len - 2] == '\r' &&
                        bytes[len - 1] == '\n') {
                    break;
                }
            }
            prev = curr;
        }
        return buffer.toByteArray();
    }

    private void parseRequestLineAndHeaders(byte[] headerBytes) {
        String headerText = new String(headerBytes, StandardCharsets.US_ASCII);
        String[] lines = headerText.split("\r\n");

        // Request Line
        String[] requestLine = lines[0].split(" ");
        if (requestLine.length != 3) {
            throw new RequestParsingException();
        }

        method = requestLine[0];
        parsePathAndQuery(requestLine[1]);

        // Headers
        for (int i = 1; i < lines.length; i++) {
            int idx = lines[i].indexOf(":");
            if (idx > 0) {
                String key = lines[i].substring(0, idx).trim();
                String value = lines[i].substring(idx + 1).trim();
                headers.put(key, value);
            }
        }

        logger.debug("method={}", method);
        logger.debug("path={}", path);
        logger.debug("headers={}", headers);
    }

    private void parsePathAndQuery(String url) {
        String[] tokens = url.split("\\?", 2);
        path = URLDecoder.decode(tokens[0], StandardCharsets.UTF_8);;

        if (tokens.length == 2) {
            parseQueryString(tokens[1]);
        }
    }

    private void parseQueryString(String query) {
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                params.put(kv[0], kv[1]);
            }
        }
    }

    private byte[] readBody(BufferedInputStream in) throws IOException {
        String cl = headers.get("Content-Length");
        if (cl == null) {
            return null;
        }

        int contentLength = Integer.parseInt(cl);
        if (contentLength <= 0) {
            return null;
        }

        return in.readNBytes(contentLength);
    }

    private void parseBody(byte[] in) throws IOException {
        if (!canParseBody()) {
            return;
        }

        if (isMultipart()) {
            multipartParser.parse(in, headers, multipartFiles, params);
            return;
        }

        if (isFormUrlEncoded()) {
            parseUrlEncodedBody(in);
        }
    }

    private boolean canParseBody() {
        return headers.containsKey(CONTENT_LENGTH.getHeader()) && headers.containsKey(CONTENT_TYPE.getHeader());
    }

    private boolean isMultipart() {
        return headers.get(CONTENT_TYPE.getHeader()).startsWith(MULTIPART_FORM_DATA);
    }

    private boolean isFormUrlEncoded() {
        return headers.get(CONTENT_TYPE.getHeader()).equals(FORM_URLENCODED);
    }

    private void parseUrlEncodedBody(byte[] bodyBytes) throws IOException {
        int contentLength = Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH.getHeader(), "0"));
        if (contentLength == 0) {
            return;
        }

        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        logger.debug("body = {}", body);

        String[] inputs = body.split("&");
        for (String input : inputs) {
            String[] nameAndValue = input.split("=");
            if(nameAndValue.length != 2){
                continue;
            }
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

    public List<MultipartFile> getMultipartFiles() {
        return multipartFiles;
    }
}

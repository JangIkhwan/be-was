package webserver.mvc;

import webserver.constant.FileMimeType;
import webserver.exception.CannotParseMultipartException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static webserver.constant.HttpHeader.CONTENT_LENGTH;
import static webserver.constant.HttpHeader.CONTENT_TYPE;

public class MultipartRequestParser {
    public void parse(InputStream in, Map<String, String> headers, List<MultipartFile> multipartFiles, Map<String, String> params) throws IOException {
        int contentLength = Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH.getHeader(), "0"));
        if (contentLength == 0) {
            return;
        }

        // 바운더리 찾기
        String boundary = parseBoundary(headers);

        // 바운더리 기준으로 자르고 파트들을 파싱
        byte[] bodyBytes = in.readNBytes(contentLength);
        byte[] boundaryBytes = ("--" + boundary).getBytes(StandardCharsets.ISO_8859_1);
        byte[] endBoundaryBytes = ("--" + boundary + "--").getBytes(StandardCharsets.ISO_8859_1);

        int firstBoundary = indexOf(bodyBytes, boundaryBytes);
        if (firstBoundary == -1) {
            return;
        }

        int startIndex = firstBoundary + boundaryBytes.length + 2;
        for (int i = boundaryBytes.length; i < bodyBytes.length - endBoundaryBytes.length; i++) {
            if (matched(i, bodyBytes, endBoundaryBytes)) {
                break;
            }
            if (matched(i, bodyBytes, boundaryBytes)) {
                byte[] partBytes = Arrays.copyOfRange(bodyBytes, startIndex, i);
                parsePart(partBytes, multipartFiles, params);
                startIndex = i + boundaryBytes.length + 2;
            }
        }
    }

    private String parseBoundary(Map<String, String> headers) {
        String boundary = null;
        String[] contentTypeTokens = headers.get(CONTENT_TYPE.getHeader()).split(";");
        for (String token : contentTypeTokens) {
            token = token.trim();
            if (!token.startsWith("boundary")) {
                continue;
            }
            String[] keyAndValues = token.trim().split("=");
            if (keyAndValues.length != 2) {
                continue;
            }
            boundary = keyAndValues[1].trim();
        }

        // 못찾으면 에러
        if (boundary == null || boundary.length() <= 0) {
            throw new CannotParseMultipartException();
        }
        return boundary;
    }

    private int indexOf(byte[] target, byte[] pattern) {
        for (int i = 0; i <= target.length - pattern.length; i++) {
            boolean matched = true;
            for (int j = 0; j < pattern.length; j++) {
                if (target[i + j] != pattern[j]) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return i;
            }
        }
        return -1;
    }

    private boolean matched(int matchStartIndex, byte[] bodyBytes, byte[] boundaryBytes) {
        for (int i = 0; i < boundaryBytes.length; i++) {
            if (bodyBytes[matchStartIndex + i] != boundaryBytes[i]) {
                return false;
            }
        }
        return true;
    }

    private void parsePart(byte[] partBytes, List<MultipartFile> multipartFiles, Map<String, String> params) {
        byte[] separator = "\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1);
        int headerEnd = indexOf(partBytes, separator);
        if (headerEnd == -1) return;

        byte[] headerBytes = Arrays.copyOfRange(partBytes, 0, headerEnd);
        byte[] bodyBytes = Arrays.copyOfRange(partBytes, headerEnd + 4, partBytes.length);

        String headersText = new String(headerBytes, StandardCharsets.ISO_8859_1);
        Map<String, String> partHeaders = parsePartHeaders(headersText);

        if (isFile(partHeaders)) {
            String filename = extractFilename(partHeaders);
            InputStream inputStream = new ByteArrayInputStream(bodyBytes);
            multipartFiles.add(new MultipartFile(filename, inputStream));
        } else {
            String name = extractName(partHeaders);
            String value = new String(bodyBytes, StandardCharsets.UTF_8);
            params.put(name, value);
        }
    }

    private Map<String, String> parsePartHeaders(String headersText) {
        HashMap<String, String> headers = new HashMap<>();
        String[] partHeaderLines = headersText.split("\r\n");
        for (String line : partHeaderLines) {
            int idx = line.indexOf(":");
            if (idx < 0) {
                continue;
            }
            String field = line.substring(0, idx).trim();
            String value = line.substring(idx).trim();
            headers.put(field, value);
        }
        return headers;
    }

    private boolean isFile(Map<String, String> partHeaders) {
        return FileMimeType.contains(partHeaders.get(CONTENT_TYPE.getHeader()));
    }

    private String extractName(Map<String, String> headers) {
        String disposition = headers.get("Content-Disposition");
        if (disposition == null) return null;

        String[] tokens = disposition.split(";");
        for (String token : tokens) {
            token = token.trim();
            if (token.startsWith("name=")) {
                String name = token.substring("name=".length()).trim();
                if (name.startsWith("\"") && name.endsWith("\"")) {
                    name = name.substring(1, name.length() - 1);
                }
                return name;
            }
        }
        throw new CannotParseMultipartException();
    }

    private String extractFilename(Map<String, String> headers) {
        String disposition = headers.get("Content-Disposition");
        if (disposition == null) return null;

        for (String token : disposition.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                String[] kv = token.split("=", 2);
                String filename = kv[1].trim();
                if (filename.startsWith("\"") && filename.endsWith("\"")) {
                    filename = filename.substring(1, filename.length() - 1);
                }
                return filename;
            }
        }
        return null;
    }
}

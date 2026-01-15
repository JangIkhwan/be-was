package webserver.mvc;

import webserver.http.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PathVariableRouting implements Routing {
    private Map<String, Handler> handlerMap;
    private List<String> pathVariableNames = new ArrayList<>();
    private Pattern pattern;

    public PathVariableRouting(String uri, Map<String, Handler> handlerMap) {
        this.handlerMap = handlerMap;
        String regex = Arrays.stream(uri.split("/"))
                .map(token -> {
                    if (token.startsWith("{") && token.endsWith("}")) {
                        String varName = token.substring(1, token.length() - 1);
                        pathVariableNames.add(varName);
                        return "([^/]+)";
                    }
                    return token;
                })
                .collect(Collectors.joining("/"));

        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean supportsUri(String uri) {
        return pattern.matcher(uri).matches();
    }

    @Override
    public boolean supportsMethod(String method) {
        return handlerMap.containsKey(method);
    }

    @Override
    public Handler resolveHandler(Request request) {
        Matcher matcher = pattern.matcher(request.getPath());

        if (matcher.matches()) {
            for (int i = 0; i < pathVariableNames.size(); i++) {
                String name = pathVariableNames.get(i);
                String value = matcher.group(i + 1); // group(1)부터 시작
                request.setParameter(name, value);
            }
        }

        return handlerMap.get(request.getMethod());
    }
}

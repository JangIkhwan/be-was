package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.FileMimeType;
import webserver.constant.ResponseStatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static webserver.constant.HttpHeader.LOCATION;
import static webserver.constant.HttpHeader.SET_COOKIE;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private int code;
    private byte[] body;
    private String contentType;
    private String codeDescription;
    private Map<String, String> headers = new HashMap<>();

    private Response(){ }

    public void setCookie(String key, String value) {
        String setCookieHeaderValue = headers.get(SET_COOKIE.getHeader());
        if(setCookieHeaderValue == null){
            headers.put(SET_COOKIE.getHeader(), key + "=" + value);
            return;
        }
        String newValue = new StringBuffer().append("; ").append(key).append("=").append(value).toString();
        setCookieHeaderValue += newValue;
        headers.put(SET_COOKIE.getHeader(), setCookieHeaderValue);
    }

    public int getCode() {
        return code;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public Set<String> getHeaderFields() {
        return headers.keySet();
    }

    public String getHeader(String headerField) {
        return headers.get(headerField);
    }

    public boolean hasBody(){
        return body != null;
    }

    public static Response forward(String path) {
        try{
            byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
            String contentType = FileMimeType.resolveMimeType(path);
            return Response.ok(body, contentType);
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
        }
        return Response.notFound();
    }

    public static Response forwardDynamicHtml(Map<String, String> model, String path) {
        try{
            Path filePath = Paths.get("./src/main/resources/static" + path);
            String bodyString = Files.readString(filePath, StandardCharsets.UTF_8);

            logger.debug("bodyString = {} " , bodyString);

            for(String key : model.keySet()){
                String toReplace = "\\$\\{\\{" + key + "\\}\\}";
                logger.debug("toReplace = {}", toReplace);
                bodyString = bodyString.replaceAll(toReplace, model.get(key));
            }

            byte[] body = bodyString.getBytes(StandardCharsets.UTF_8);
            String contentType = FileMimeType.resolveMimeType(path);
            return Response.ok(body, contentType);
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
        }
        return Response.notFound();
    }

    public static Response ok(byte[] body, String contentType){
        Response response = new Response();
        response.code = ResponseStatusCode.OK.getCode();
        response.codeDescription = ResponseStatusCode.OK.getDescription();
        response.body = body;
        response.contentType = contentType;
        return response;
    }

    public static Response redirect(String redirectUrl){
        Response response = new Response();
        response.code = ResponseStatusCode.SEE_OTHER.getCode();
        response.codeDescription = ResponseStatusCode.SEE_OTHER.getDescription();
        response.headers.put(LOCATION.getHeader(), redirectUrl);
        return response;
    }

    public static Response notFound(){
        Response response = new Response();
        response.code = ResponseStatusCode.NOT_FOUND.getCode();
        response.codeDescription = ResponseStatusCode.NOT_FOUND.getDescription();
        return response;
    }

    public static Response internalServerError() {
        Response response = new Response();
        response.code = ResponseStatusCode.INTERNAL_SERVER_ERROR.getCode();
        response.codeDescription = ResponseStatusCode.INTERNAL_SERVER_ERROR.getDescription();
        return response;
    }

    public static Response badRequest() {
        Response response = new Response();
        response.code = ResponseStatusCode.BAD_REQUEST.getCode();
        response.codeDescription = ResponseStatusCode.BAD_REQUEST.getDescription();
        return response;
    }

}

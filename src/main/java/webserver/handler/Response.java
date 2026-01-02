package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.FileMimeType;
import webserver.constant.ResponseStatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private int code;
    private byte[] body;
    private String contentType;
    private String redirectUrl;
    private String codeDescription;

    public Response(){ }

    public int getCode() {
        return code;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public boolean isRedirect() {
        return code == ResponseStatusCode.SEE_OTHERS.getCode();
    }

    public boolean hasBody(){
        return body != null;
    }

    public static Response ok(byte[] body, String contentType){
        Response response = new Response();
        response.code = ResponseStatusCode.OK.getCode();
        response.codeDescription = ResponseStatusCode.OK.getDescription();
        response.body = body;
        response.contentType = contentType;
        return response;
    }

    public static Response forward(String path) {
        Response response = new Response();
        try{
            response.body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
            return Response.notFound();
        }
        response.code = ResponseStatusCode.OK.getCode();
        response.codeDescription = ResponseStatusCode.OK.getDescription();
        response.contentType = FileMimeType.resolveMimeType(path);
        return response;
    }

    public static Response redirect(String redirectUrl){
        Response response = new Response();
        response.code = ResponseStatusCode.SEE_OTHERS.getCode();
        response.codeDescription = ResponseStatusCode.SEE_OTHERS.getDescription();
        response.redirectUrl = redirectUrl;
        return response;
    }

    public static Response notFound(){
        Response response = new Response();
        response.code = ResponseStatusCode.NOT_FOUND.getCode();
        response.codeDescription = ResponseStatusCode.NOT_FOUND.getDescription();
        return response;
    }
}

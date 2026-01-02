package webserver.handler;

import webserver.constant.ResponseStatusCode;

public class Response {
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
        response.body = body;
        response.contentType = contentType;
        response.codeDescription = ResponseStatusCode.OK.getDescription();
        return response;
    }

    public static Response redirect(String redirectUrl){
        Response response = new Response();
        response.code = ResponseStatusCode.SEE_OTHERS.getCode();
        response.redirectUrl = redirectUrl;
        response.codeDescription = ResponseStatusCode.SEE_OTHERS.getDescription();
        return response;
    }

    public static Response notFound(){
        Response response = new Response();
        response.code = ResponseStatusCode.NOT_FOUND.getCode();
        response.codeDescription = ResponseStatusCode.NOT_FOUND.getDescription();
        return response;
    }
}

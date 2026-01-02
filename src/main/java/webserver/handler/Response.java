package webserver.handler;

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
        return code == 303;
    }

    public boolean hasBody(){
        return body != null;
    }

    public static Response ok(byte[] body, String contentType){
        Response response = new Response();
        response.code = 200;
        response.body = body;
        response.contentType = contentType;
        response.codeDescription = "OK";
        return response;
    }

    public static Response redirect(String redirectUrl){
        Response response = new Response();
        response.code = 303;
        response.redirectUrl = redirectUrl;
        response.codeDescription = "SEE OTHERS";
        return response;
    }

    public static Response notFound(){
        Response response = new Response();
        response.code = 404;
        response.codeDescription = "NOT FOUND";
        return response;
    }
}

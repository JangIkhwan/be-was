package webserver.handler;

public class Response {
    private int code;
    private byte[] body;
    private String contentType;
    private String redirectUrl;

    public Response(){ }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public boolean isRedirect() {
        return code == 303;
    }

    public boolean isNotFound() {
        return code == 404;
    }

    public static Response ok(byte[] body, String contentType){
        Response response = new Response();
        response.code = 200;
        response.body = body;
        response.contentType = contentType;
        return response;
    }

    public static Response redirect(String redirectUrl){
        Response response = new Response();
        response.code = 303;
        response.redirectUrl = redirectUrl;
        return response;
    }

    public static Response notFound(){
        Response response = new Response();
        response.code = 404;
        return response;
    }
}

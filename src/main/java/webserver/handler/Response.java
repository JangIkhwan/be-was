package webserver.handler;

public class Response {
    private byte[] body;
    private String contentType;
    private String redirectUrl;

    public Response(){ }

    public Response(byte[] body, String contentType) {
        this.body = body;
        this.contentType = contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public Response setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public Response setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Response setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public boolean isRedirect() {
        return redirectUrl != null;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }
}

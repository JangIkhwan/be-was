package webserver;

public class Response {
    private byte[] body;
    private String contentType;

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
}

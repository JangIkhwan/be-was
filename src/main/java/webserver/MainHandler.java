package webserver;

public class MainHandler {
    public Response handle() {
        byte[] body = "<h1>Hello World</h1>".getBytes();
        String contentType = "text/html";
        return new Response(body, contentType);
    }
}

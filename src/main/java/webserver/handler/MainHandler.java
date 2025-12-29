package webserver.handler;

public class MainHandler implements Handler {
    public Response handle(Request request) {
        byte[] body = "<h1>Hello World</h1>".getBytes();
        String contentType = "text/html";
        return new Response(body, contentType);
    }
}

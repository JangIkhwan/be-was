package webserver.handler;

public class MainHandler implements Handler {
    public Response handle(Request request) {
        return Response.forward("/index.html");
    }
}

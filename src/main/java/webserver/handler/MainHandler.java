package webserver.handler;

public class MainHandler implements Handler {
    public Response handle(Request request) {
        return new Response().setRedirectUrl("/index.html");
    }
}

package webserver.handler;

public interface Handler {
    Response handle(Request request);
}

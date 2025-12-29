package webserver.handler;

public class Request {
    private String path;

    public Request(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

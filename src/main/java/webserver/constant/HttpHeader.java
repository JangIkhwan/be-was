package webserver.constant;

public enum HttpHeader {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    SET_COOKIE("Set-Cookie"),
    COOKIE("Cookie"),
    LOCATION("Location");

    private final String header;

    HttpHeader(String header) {
        this.header = header;
    }

    public String getHeader(){
        return this.header;
    }
}

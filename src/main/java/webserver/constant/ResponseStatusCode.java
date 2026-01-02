package webserver.constant;

public enum ResponseStatusCode {
    OK(200, "OK"),
    SEE_OTHERS(303, "SEE OTHER"),
    NOT_FOUND(404, "NOT FOUND");

    private final int code;
    private final String description;

    ResponseStatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

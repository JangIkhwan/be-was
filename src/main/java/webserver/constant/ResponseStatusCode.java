package webserver.constant;

public enum ResponseStatusCode {
    OK(200, "OK"),
    SEE_OTHER(303, "SEE OTHER"),
    NOT_FOUND(404, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR"),
    BAD_REQUEST(400, "BAD REQUEST" );

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

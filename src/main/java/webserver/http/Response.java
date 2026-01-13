package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.ResponseStatusCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static webserver.constant.HttpHeader.LOCATION;
import static webserver.constant.HttpHeader.SET_COOKIE;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private int code;
    private byte[] body;
    private String contentType;
    private String codeDescription;
    private Map<String, String> headers = new HashMap<>();

    public Response(){ }

    public void setCookie(String key, String value) {
        String setCookieHeaderValue = headers.get(SET_COOKIE.getHeader());
        if(setCookieHeaderValue == null){
            headers.put(SET_COOKIE.getHeader(), key + "=" + value);
            return;
        }
        String newValue = new StringBuffer().append("; ").append(key).append("=").append(value).toString();
        setCookieHeaderValue += newValue;
        headers.put(SET_COOKIE.getHeader(), setCookieHeaderValue);
    }

    public int getCode() {
        return code;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public Set<String> getHeaderFields() {
        return headers.keySet();
    }

    public String getHeader(String headerField) {
        return headers.get(headerField);
    }

    public boolean hasBody(){
        return body != null;
    }

    public void setOk(byte[] body, String contentType){
        this.code = ResponseStatusCode.OK.getCode();
        this.codeDescription = ResponseStatusCode.OK.getDescription();
        this.body = body;
        this.contentType = contentType;
    }

    public void setRedirect(String redirectUrl){
        this.code = ResponseStatusCode.SEE_OTHER.getCode();
        this.codeDescription = ResponseStatusCode.SEE_OTHER.getDescription();
        this.headers.put(LOCATION.getHeader(), redirectUrl);
    }

    public static Response notFound(){
        Response response = new Response();
        response.code = ResponseStatusCode.NOT_FOUND.getCode();
        response.codeDescription = ResponseStatusCode.NOT_FOUND.getDescription();
        return response;
    }

    public static Response internalServerError() {
        Response response = new Response();
        response.code = ResponseStatusCode.INTERNAL_SERVER_ERROR.getCode();
        response.codeDescription = ResponseStatusCode.INTERNAL_SERVER_ERROR.getDescription();
        return response;
    }

    public static Response badRequest() {
        Response response = new Response();
        response.code = ResponseStatusCode.BAD_REQUEST.getCode();
        response.codeDescription = ResponseStatusCode.BAD_REQUEST.getDescription();
        return response;
    }

    public static Response methodNotAllowed() {
        Response response = new Response();
        response.code = ResponseStatusCode.METHOD_NOT_ALLOWED.getCode();
        response.codeDescription = ResponseStatusCode.METHOD_NOT_ALLOWED.getDescription();
        return response;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", contentType='" + contentType + '\'' +
                ", codeDescription='" + codeDescription + '\'' +
                ", headers=" + headers +
                '}';
    }
}

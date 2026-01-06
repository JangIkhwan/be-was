package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static webserver.constant.HttpHeader.CONTENT_LENGTH;
import static webserver.constant.HttpHeader.CONTENT_TYPE;

public class ResponseWriter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);
    private final String CRLF = "\r\n";
    private String HEADER_DELIMITER = ": ";
    private DataOutputStream dos;

    public ResponseWriter(OutputStream out) {
         dos = new DataOutputStream(out);
    }

    public void write(Response response) {
        writeStatusLine(response);
        writeHeader(response);
        writeBody(response);
    }

    private void writeStatusLine(Response response) {
        try{
            dos.writeBytes("HTTP/1.1 " + response.getCode() + " " + response.getCodeDescription() + CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeHeader(Response response) {
        try {
            for(String headerField : response.getHeaderFields()){
                dos.writeBytes(headerField + HEADER_DELIMITER + response.getHeader(headerField));
            }
            if(response.hasBody()){
                dos.writeBytes(CONTENT_TYPE.getHeader() + HEADER_DELIMITER + response.getContentType() + CRLF);
                dos.writeBytes(CONTENT_LENGTH.getHeader() + HEADER_DELIMITER + response.getBody().length + CRLF);
            }
            dos.writeBytes(CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeBody(Response response) {
        try {
            if(response.hasBody()){
                dos.write(response.getBody(), 0, response.getBody().length);
            }
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

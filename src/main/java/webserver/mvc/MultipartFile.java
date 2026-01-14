package webserver.mvc;

import java.io.InputStream;

public class MultipartFile {
    private String filename;
    private InputStream inputStream;

    public MultipartFile(String filename, InputStream inputStream) {
        this.filename = filename;
        this.inputStream = inputStream;
    }

    public String getFilename(){
        return filename;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}

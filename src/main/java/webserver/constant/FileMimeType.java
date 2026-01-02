package webserver.constant;

public enum FileMimeType {
    HTML(".html", "text/html"),
    JS(".js", "text/javascript"),
    CSS(".css", "text/css"),
    SVG(".svg", "image/svg+xml"),
    ICO(".ico", "image/vnd.microsoft.icon"),
    PNG(".png", "image/png"),
    JPG(".jpg", "image/jpg"),
    JPEG(".jpeg", "image/jpg");

    private final String fileExtension;
    private final String mimeType;

    FileMimeType(String fileExtension, String mimeType) {
        this.fileExtension = fileExtension;
        this.mimeType = mimeType;
    }

    public static String resolveMimeType(String path){
        for(FileMimeType type : FileMimeType.values()){
            if(path.endsWith(type.fileExtension)){
                return type.mimeType;
            }
        }
        return HTML.mimeType;
    }
}

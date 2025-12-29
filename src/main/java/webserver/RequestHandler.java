package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = br.readLine();
            logger.debug("http first line {}", line);
            String path = line.split(" ")[1];

            // 요청 처리
            logger.debug("path {}", path);

            if(path.equals("/")){
                MainHandler handler = new MainHandler();
                ResponseWriter responseWriter = new ResponseWriter(out);
                Response response = handler.handle();
                responseWriter.write(response.getBody(), response.getContentType());
            }
            if(path.endsWith(".html")){
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
                response200Header(dos, "text/html", body.length);
                responseBody(dos, body);
            }
            if(path.endsWith(".css")){
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
                response200Header(dos, "text/css", body.length);
                responseBody(dos, body);
            }
            if(path.endsWith(".svg")){
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
                response200Header(dos, "image/svg+xml" ,body.length);
                responseBody(dos, body);
            }
            if(path.endsWith(".ico")){
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
                response200Header(dos, "image/vnd.microsoft.icon" ,body.length);
                responseBody(dos, body);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

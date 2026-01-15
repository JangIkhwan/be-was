package webserver.util;

import webserver.mvc.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class MultipartFileUtil {
    public static String saveFile(String directoryPath, MultipartFile multipartFile) throws IOException {
        if (directoryPath == null || directoryPath.isBlank()) {
            throw new IllegalArgumentException();
        }

        String filename = UUID.randomUUID() + multipartFile.getFilename();

        Path uploadDir = Paths.get(System.getProperty("user.dir"))
                .resolve(directoryPath);

        Files.createDirectories(uploadDir);

        Path target = uploadDir.resolve(filename);

        try (InputStream in = multipartFile.getInputStream();
             OutputStream out = Files.newOutputStream(target,
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING)) {

            in.transferTo(out);
        }

        return directoryPath + "/" + filename;
    }
}

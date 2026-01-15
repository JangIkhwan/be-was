package webserver.handler;

import db.ArticleRepository;
import model.Article;
import model.User;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.mvc.MultipartFile;
import webserver.mvc.RedirectView;
import webserver.util.AuthUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class CreateArticleHandler implements Handler {
    private final ArticleRepository articleRepository;

    public CreateArticleHandler(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return new RedirectView("/login");
        }
        User user = AuthUtil.getAuthenticatedUser(request);
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if(request.getMultipartFiles().size() != 1){
            // todo 게시글 작성폼에서 에러문구 출력하도록 수정
            throw new BadRequestException();
        }

        MultipartFile multipartFile = request.getMultipartFiles().get(0);

        String imageUrl = null;
        try {
            imageUrl = saveFile(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        articleRepository.save(new Article(user.getId(), title, content, imageUrl));

        return new RedirectView("/");
    }

    private String saveFile(MultipartFile multipartFile) throws IOException {
        InputStream in = multipartFile.getInputStream();
        String filename = UUID.randomUUID().toString() + multipartFile.getFilename();
        String projectRoot = System.getProperty("user.dir");
        Path uploadDir = Paths.get(projectRoot, "uploads", "images");

        Files.createDirectories(uploadDir);

        Path target = uploadDir.resolve(filename);

        try (OutputStream out = Files.newOutputStream(
                target,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            in.transferTo(out);
        }

        return "/uploads/images" + filename;
    }
}

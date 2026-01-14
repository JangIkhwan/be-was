package webserver.handler;

import db.ArticleRepository;
import model.Article;
import model.User;
import webserver.exception.BadRequestException;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.mvc.MultipartFile;
import webserver.mvc.RedirectView;
import webserver.util.AuthUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

        // 파일 업로드 처리
        List<MultipartFile> multipartFiles = request.getMultipartFiles();

        if(multipartFiles.size() != 1){
            throw new BadRequestException();
        }

        MultipartFile multipartFile = multipartFiles.get(0);
        String filename = multipartFile.getFilename();
        InputStream inputStream = multipartFile.getInputStream();
        if (filename == null || filename.isEmpty()) {
            throw new BadRequestException();
        }

        File uploadDir = new File("./uploads");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File file = new File(uploadDir, filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            inputStream.transferTo(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        articleRepository.save(new Article(user.getId(), title, content));

        return new RedirectView("/");
    }
}

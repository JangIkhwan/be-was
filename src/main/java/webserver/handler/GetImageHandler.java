package webserver.handler;

import webserver.exception.StaticResourceNotFoundException;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.view.UploadedFileView;
import webserver.mvc.ModelAndView;

public class GetImageHandler implements Handler {
    @Override
    public ModelAndView handle(Request request, Response response) {
        String imageFileUrl = request.getParameter("imageUrl");
        if(imageFileUrl == null){
            throw new StaticResourceNotFoundException();
        }
        return new UploadedFileView("/uploads/images/" + imageFileUrl);
    }
}

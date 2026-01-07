package webserver.service;

import model.User;
import webserver.handler.Request;
import webserver.session.SessionStore;

public class AuthService {
    public boolean isAuthenticatedUser(Request request) {
        String sid = request.getCookie("sid");
        if(!foundCookie(sid)) {
            return false;
        }
        SessionStore sessionStore = request.getSessionStore();
        User loginedUser = (User) sessionStore.getSession(sid);
        if(!loggedIn(loginedUser)){
            return false;
        }
        return true;
    }

    private boolean foundCookie(String sid) {
        return sid != null;
    }

    private boolean loggedIn(User loginedUser) {
        return loginedUser != null;
    }
}

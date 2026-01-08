package webserver.service;

import model.User;
import webserver.http.Request;
import webserver.session.SessionStore;

public class AuthUtil {
    public static boolean isAuthenticatedUser(Request request) {
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

    private static boolean foundCookie(String sid) {
        return sid != null;
    }

    private static boolean loggedIn(User loginedUser) {
        return loginedUser != null;
    }

    public static User getAuthenticatedUser(Request request) {
        String sid = request.getCookie("sid");
        if(!foundCookie(sid)) {
            return null;
        }

        SessionStore sessionStore = request.getSessionStore();
        User loginedUser = (User) sessionStore.getSession(sid);
        return loginedUser;
    }
}

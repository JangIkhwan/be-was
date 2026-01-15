package webserver.handler;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.ModelAndView;
import webserver.view.RedirectView;
import webserver.view.StaticResourceView;
import webserver.session.SessionStore;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class LoginHandlerTest {
    @DisplayName("올바른 요청이면 메인화면으로 리다이렉트한다")
    @Test
    void shouldRedirectToMain_whenCorrectRequest(){
        // given
        String email = "email@email";
        String password = "pass";
        Database.addUser(new User(email, password, "name", email));

        LoginHandler loginHandler = new LoginHandler();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        SessionStore sessionStore = new SessionStore();

        Request request = new Request("POST", "/login", new HashMap<>(), params, sessionStore);

        // when
        ModelAndView mav = loginHandler.handle(request, new Response());

        // then
        assertThat(mav).isInstanceOf(RedirectView.class);
        assertThat(mav.getViewName()).isEqualTo("/");
    }

    @DisplayName("유저가 존재하지 않으면 로그인 에러 폼을 전송한다")
    @Test
    void shouldReturnLoginErrorForm_whenUserNotFound(){
        // given
        String email = "email2@email";
        String password = "pass2";

        LoginHandler loginHandler = new LoginHandler();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        SessionStore sessionStore = new SessionStore();

        Request request = new Request("POST", "/login", new HashMap<>(), params, sessionStore);

        // when
        ModelAndView mav = loginHandler.handle(request, new Response());

        // then
        assertThat(mav).isInstanceOf(StaticResourceView.class);
        assertThat(mav.getViewName()).isEqualTo("/login/error.html");
    }

    @DisplayName("비밀번호가 일치하지 않으면 로그인 에러 폼을 전송한다")
    @Test
    void shouldReturnOk_whenIncorrectPassword(){
        // given
        String email = "email@email";
        String password = "pass";
        Database.addUser(new User(email, password, "name", email));

        LoginHandler loginHandler = new LoginHandler();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", "incorrect");

        SessionStore sessionStore = new SessionStore();

        Request request = new Request("POST", "/login", new HashMap<>(), params, sessionStore);

        // when
        ModelAndView mav = loginHandler.handle(request, new Response());

        // then
        assertThat(mav).isInstanceOf(StaticResourceView.class);
        assertThat(mav.getViewName()).isEqualTo("/login/error.html");
    }
}
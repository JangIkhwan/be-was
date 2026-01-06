package webserver.handler;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.session.SessionStore;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class LoginHandlerTest {
    @DisplayName("올바른 요청이면 redirect를 응답한다")
    @Test
    void shouldReturnRedirect_whenCorrectRequest(){
        // given
        String email = "email@email";
        String password = "pass";
        Database.addUser(new User(email, password, "name", email));

        LoginHandler loginHandler = new LoginHandler();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        SessionStore sessionStore = new SessionStore();

        Request request = new Request("POST", "/login", params, sessionStore);

        // when
        Response response = loginHandler.handle(request);

        // then
        assertThat(response.getCode()).isEqualTo(303);
    }

    @DisplayName("유저가 존재하지 않으면 Bad Request를 응답한다")
    @Test
    void shouldReturnBadRequest_whenUserNotFound(){
        // given
        String email = "email2@email";
        String password = "pass2";

        LoginHandler loginHandler = new LoginHandler();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        SessionStore sessionStore = new SessionStore();

        Request request = new Request("POST", "/login", params, sessionStore);

        // when
        Response response = loginHandler.handle(request);

        // then
        assertThat(response.getCode()).isEqualTo(400);
    }

    @DisplayName("비밀번호가 일치하지 않으면 Bad Request를 응답한다")
    @Test
    void shouldReturnBadRequest_whenIncorrectPassword(){
        // given
        String email = "email@email";
        String password = "pass";
        Database.addUser(new User(email, password, "name", email));

        LoginHandler loginHandler = new LoginHandler();

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", "incorrect");

        SessionStore sessionStore = new SessionStore();

        Request request = new Request("POST", "/login", params, sessionStore);

        // when
        Response response = loginHandler.handle(request);

        // then
        assertThat(response.getCode()).isEqualTo(400);
    }
}
package webserver.handler;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.service.AuthService;
import webserver.session.SessionStore;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.constant.ResponseStatusCode.SEE_OTHER;

class LogoutHandlerTest {
    @DisplayName("올바른 요청이면 리다이렉트한다")
    @Test
    void shouldRedirect_whenCorrectRequest(){
        // given
        AuthService authService = new AuthService();
        LogoutHandler logoutHandler = new LogoutHandler(authService);
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "sid=1234");

        SessionStore sessionStore = new SessionStore();
        sessionStore.addSession("1234", new User("asdf", "asdf", "asdf", "asdf"));

        Request request = new Request("POST", "/logout", header, new HashMap<>(), sessionStore);

        // when
        Response response = logoutHandler.handle(request);

        // then
        assertThat(response.getCode()).isEqualTo(SEE_OTHER.getCode());
        assertThat(response.getHeader("Location")).isEqualTo("/");
    }
}
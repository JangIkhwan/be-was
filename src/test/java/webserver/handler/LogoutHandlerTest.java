package webserver.handler;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.ModelAndView;
import webserver.mvc.RedirectView;
import webserver.session.SessionStore;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LogoutHandlerTest {
    @DisplayName("올바른 요청이면 리다이렉트한다")
    @Test
    void shouldRedirect_whenCorrectRequest(){
        // given
        LogoutHandler logoutHandler = new LogoutHandler();
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "sid=1234");

        SessionStore sessionStore = new SessionStore();
        sessionStore.addSession("1234", new User("asdf", "asdf", "asdf", "asdf"));

        Request request = new Request("POST", "/logout", header, new HashMap<>(), sessionStore);

        // when
        ModelAndView mav = logoutHandler.handle(request, new Response());

        // then
        assertThat(mav).isInstanceOf(RedirectView.class);
        assertThat(mav.getViewName()).isEqualTo("/");
    }
}
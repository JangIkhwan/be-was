package webserver.handler;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.session.SessionStore;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static webserver.constant.ResponseStatusCode.OK;
import static webserver.constant.ResponseStatusCode.SEE_OTHER;

class MyPageHandlerTest {
    @DisplayName("로그인하지 않은 사용자는 리다이렉트한다")
    @Test
    void shouldRedirect_whenUserNotLoggedIn(){
        // given
        MyPageHandler myPageHandler = new MyPageHandler();

        SessionStore sessionStore = new SessionStore();

        Request request = new Request("GET", "/mypage", new HashMap<>(), new HashMap<>(), sessionStore);

        // when
        Response response = myPageHandler.handle(request);

        // then
        assertThat(response.getCode()).isEqualTo(SEE_OTHER.getCode());
    }

    @DisplayName("로그인한 사용자는 마이페이지를 반환한다")
    @Test
    void shouldReturnMyPage_whenUserLoggedIn(){
        // given
        MyPageHandler myPageHandler = new MyPageHandler();

        HashMap<String, String> header = new HashMap<>();
        header.put("Cookie", "sid=1234");

        SessionStore sessionStore = new SessionStore();
        sessionStore.addSession("1234", new User("asdf", "asdf", "asdf", "asdf"));

        Request request = new Request("GET", "/mypage", header, new HashMap<>(), sessionStore);

        // when
        Response response = myPageHandler.handle(request);

        // then
        assertThat(response.getCode()).isEqualTo(OK.getCode());
    }
}
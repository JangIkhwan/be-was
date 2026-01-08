package webserver.handler;

import org.junit.jupiter.api.Test;
import webserver.http.Response;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseTest {
    @Test
    void setCookieTest(){
        // given
        Response redirect = new Response();

        // when
        redirect.setCookie("sid", "1234");
        redirect.setCookie("Path", "/");

        // then
        assertThat(redirect.getHeader("Set-Cookie")).isEqualTo("sid=1234; Path=/");
    }
}
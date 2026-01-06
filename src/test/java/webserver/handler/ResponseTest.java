package webserver.handler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {
    @Test
    void setCookieTest(){
        // given
        Response redirect = Response.redirect("/index.html");

        // when
        redirect.setCookie("sid", "1234");
        redirect.setCookie("Path", "/");

        // then
        assertThat(redirect.getHeader("Set-Cookie")).isEqualTo("sid=1234; Path=/");
    }
}
package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.handler.Request;
import webserver.http.RequestGenerator;
import webserver.session.SessionStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class RequestGeneratorTest {
    @DisplayName("요청 path 파싱에 성공한다")
    @Test
    void shouldParsePath() throws IOException {
        // given
        byte[] bytes = "HTTP1.1 / GET\r\n\r\n".getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(bytes);
        SessionStore sessionStore = new SessionStore();

        // when
        RequestGenerator requestGenerator = new RequestGenerator(in);
        Request request = requestGenerator.generate(sessionStore);

        // then
        assertThat(request.getPath()).isEqualTo("/");
    }

    @DisplayName("요청 쿼리파라미터 파싱에 성공한다")
    @Test
    void shouldParseParameters() throws IOException {
        // given
        byte[] bytes = "HTTP1.1 /create?userId=1234&password=asdf&invalid=invalid=invalid GET\r\n\r\n".getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(bytes);
        SessionStore sessionStore = new SessionStore();

        // when
        RequestGenerator requestGenerator = new RequestGenerator(in);
        Request request = requestGenerator.generate(sessionStore);

        // then
        assertThat(request.getPath()).isEqualTo("/create");
        assertThat(request.getParameter("userId")).isEqualTo("1234");
        assertThat(request.getParameter("password")).isEqualTo("asdf");
        assertThat(request.getParameter("unknown")).isNull();
        assertThat(request.getParameter("invalid")).isNull();
    }
}
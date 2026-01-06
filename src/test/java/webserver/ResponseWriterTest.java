package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.handler.Response;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ResponseWriterTest {
    @DisplayName("상태코드 200 응답을 반환한다")
    @Test
    void shouldWrite200Response(){
        // given
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResponseWriter responseWriter = new ResponseWriter(baos);

        byte[] body = "hello".getBytes(StandardCharsets.UTF_8);
        Response ok = Response.ok(body, "text/html");

        // when
        responseWriter.write(ok);

        // then
        String response = baos.toString();
        assertThat(response.startsWith("HTTP1.1 200 OK"));

        int headerEndIndex = response.indexOf("\r\n\r\n");
        assertThat(response.substring(headerEndIndex + 4)).startsWith("hello");
    }

    @DisplayName("상태코드 404 응답을 반환한다")
    @Test
    void shouldWrite404Response(){
        // given
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResponseWriter responseWriter = new ResponseWriter(baos);

        Response notFound = Response.notFound();

        // when
        responseWriter.write(notFound);

        // then
        String response = baos.toString();
        assertThat(response.startsWith("HTTP1.1 404 NOT FOUND"));
    }

    @DisplayName("상태코드 303 응답을 반환한다")
    @Test
    void shouldWrite303Response(){
        // given
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResponseWriter responseWriter = new ResponseWriter(baos);

        Response redirect = Response.redirect("/index.html");

        // when
        responseWriter.write(redirect);

        // then
        String response = baos.toString();
        assertThat(response.startsWith("HTTP1.1 303 SEE OTHERS"));
        assertThat(response.contains("Location: /index.html"));
    }

    @DisplayName("Set-Cookie 헤더가 있으면 응답에 쓴다")
    @Test
    void shouldWriteSetCookieHeaderToRespnonse(){
        // given
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResponseWriter responseWriter = new ResponseWriter(baos);

        Response redirect = Response.redirect("/index.html");

        redirect.setCookie("sid", "1234");
        redirect.setCookie("Path", "/");

        // when
        responseWriter.write(redirect);

        // then
        String response = baos.toString();
        assertThat(response.startsWith("HTTP1.1 303 SEE OTHERS"));
        assertThat(response.contains("Set-Cookie: sid=1234; Path=/"));
    }
}
package webserver.session;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exception.SessionIdAlreadyExistsException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionStoreTest {
    @DisplayName("addSessionStore는 이미 존재하는 세션 id를 받으면 예외를 던진다")
    @Test
    void addSessionStore_shouldThrowException_whenSameSessionIdExists(){
        // given
        SessionStore sessionStore = new SessionStore();
        String sessionId = UUID.randomUUID().toString();
        sessionStore.addSession(sessionId, "first");

        // when & then
        assertThatThrownBy(() -> sessionStore.addSession(sessionId, "second"))
                .isInstanceOf(SessionIdAlreadyExistsException.class);
    }

    @DisplayName("getSessionStore는 저장된 값을 반환한다")
    @Test
    void getSessionStore_shouldReturnValue(){
        // given
        SessionStore sessionStore = new SessionStore();
        String sessionId = UUID.randomUUID().toString();
        String value = "hello world";
        sessionStore.addSession(sessionId, value);

        // when
        String result = (String) sessionStore.getSession(sessionId);

        // then
        assertThat(result).isEqualTo(value);
    }
}
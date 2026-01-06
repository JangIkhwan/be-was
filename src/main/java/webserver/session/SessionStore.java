package webserver.session;

import webserver.exception.SessionIdAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

public class SessionStore {
    private Map<String, Object> sessions = new HashMap<>();

    public void addSession(String sessionId, Object value){
        if(sessions.containsKey(sessionId)){
            throw new SessionIdAlreadyExistsException();
        }
        sessions.put(sessionId, value);
    }

    public Object getSession(String sessionId){
        return sessions.get(sessionId);
    }
}

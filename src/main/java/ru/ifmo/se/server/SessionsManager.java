package ru.ifmo.se.server;

import java.util.HashSet;
import java.util.Set;

public class SessionsManager {
    private final Set<ClientSession> sessions = new HashSet<ClientSession>();

    public SessionsManager() {}

    public void addSession(ClientSession session) {
        sessions.add(session);
    }

    public void removeSession(ClientSession session) {
        sessions.remove(session);
    }
}

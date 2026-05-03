package com.vorauth.session;

import java.util.HashSet;
import java.util.UUID;

public class SessionCache {

    private final HashSet<UUID> logged = new HashSet<>();

    public void login(UUID uuid) {
        logged.add(uuid);
    }

    public void logout(UUID uuid) {
        logged.remove(uuid);
    }

    public boolean isLogged(UUID uuid) {
        return logged.contains(uuid);
    }
}
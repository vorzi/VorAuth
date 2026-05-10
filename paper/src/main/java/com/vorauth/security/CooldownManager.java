package com.vorauth.security;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownManager {
    private final Map<UUID, Long> cooldownUntilEpochSeconds = new ConcurrentHashMap<>();

    public long remainingSeconds(UUID uuid) {
        Long until = cooldownUntilEpochSeconds.get(uuid);
        if (until == null) return 0;

        long now = Instant.now().getEpochSecond();
        long remaining = until - now;
        if (remaining <= 0) {
            cooldownUntilEpochSeconds.remove(uuid);
            return 0;
        }
        return remaining;
    }

    public boolean isActive(UUID uuid) {
        return remainingSeconds(uuid) > 0;
    }

    public void start(UUID uuid, long durationSeconds) {
        if (durationSeconds <= 0) return;
        long until = Instant.now().getEpochSecond() + durationSeconds;
        cooldownUntilEpochSeconds.put(uuid, until);
    }

    public void clear(UUID uuid) {
        cooldownUntilEpochSeconds.remove(uuid);
    }
}


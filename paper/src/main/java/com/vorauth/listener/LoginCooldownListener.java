package com.vorauth.listener;

import com.vorauth.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public final class LoginCooldownListener implements Listener {
    private final Main plugin;

    public LoginCooldownListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        long remaining = plugin.getLoginCooldownRemainingSeconds(uuid);
        if (remaining <= 0) return;

        event.disallow(
            AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
            "Cooldown ativo. Aguarde " + remaining + "s."
        );
    }
}


package com.vorauth;

import com.vorauth.listener.LoginCooldownListener;
import com.vorauth.security.CooldownManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import com.vorauth.database.MySQLManager;
import com.vorauth.command.*;
import com.vorauth.database.Database;
import com.vorauth.session.SessionCache;

import java.util.UUID;

public class Main extends JavaPlugin {
    private MySQLManager mysql;
    private Database database;
    private SessionCache sessionCache;
    private final CooldownManager loginCooldowns = new CooldownManager();

    @Override
    public void onEnable() {
        getLogger().info("[VorAuth] UP");
        PluginCommand changepasswdCommand = getCommand("changepasswd");
        PluginCommand setpasswdCommand = getCommand("setpasswd");
        PluginCommand registerCommand = getCommand("register");
        PluginCommand loginCommand = getCommand("login");

        saveDefaultConfig();
        
        mysql = new MySQLManager(
            getConfig().getString("mysql.host"),
            getConfig().getString("mysql.port"),
            getConfig().getString("mysql.database"),
            getConfig().getString("mysql.user"),
            getConfig().getString("mysql.password")
        );
        
        database = new Database(mysql);
        sessionCache = new SessionCache();

        database.createTablesIfNotExist().thenAccept(ok -> {
            if (!ok) getLogger().warning("[VorAuth] Failed to create tables.");
        });

        getServer().getPluginManager().registerEvents(new LoginCooldownListener(this), this);

        if (registerCommand != null) registerCommand.setExecutor(new RegisterCommand(this));
        if (loginCommand != null) loginCommand.setExecutor(new LoginCommand(this));
    }

    @Override
    public void onDisable() {
        mysql.close();
        getLogger().info("[VorAuth] DOWN");
    }

    public Database getDatabase() {
        return database;
    }

    public SessionCache getSessionCache() {
        return sessionCache;
    }

    public long getLoginCooldownRemainingSeconds(UUID uuid) {
        return loginCooldowns.remainingSeconds(uuid);
    }

    public void startLoginCooldown(UUID uuid) {
        long seconds = getConfig().getLong("login.cooldown", 0);
        loginCooldowns.start(uuid, seconds);
    }

    public void clearLoginCooldown(UUID uuid) {
        loginCooldowns.clear(uuid);
    }
}

package com.vorauth;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import com.vorauth.database.MySQLManager;
import com.vorauth.command.RegisterCommand;
import com.vorauth.database.Database;
import com.vorauth.session.SessionCache;

public class Main extends JavaPlugin {
    private MySQLManager mysql;
    private Database database;
    private SessionCache sessionCache;

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


        if (registerCommand != null) registerCommand.setExecutor(new RegisterCommand(this));
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
}

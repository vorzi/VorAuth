package com.vorauth;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("[VorAuth] UP");
        PluginCommand changepasswdCommand = getCommand("changepasswd");
        PluginCommand setpasswdCommand = getCommand("setpasswd");
        PluginCommand registerCommand = getCommand("register");
        PluginCommand loginCommand = getCommand("login");
        /*
        if (changepasswdCommand != null) changepasswdCommand.setExecutor();
        if (changepasswdCommand != null) changepasswdCommand.setExecutor();
        if (changepasswdCommand != null) changepasswdCommand.setExecutor();
        */
    }

    @Override
    public void onDisable() {
        getLogger().info("[VorAuth] DOWN");
    }
}

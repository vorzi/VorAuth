package com.vorauth.command;

import com.vorauth.Main;
import com.vorauth.session.HashCrypt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {
    private final Main plugin;

    public LoginCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1 || args[0].isEmpty()) {
            player.sendMessage("Usage: /login <password>");
            return true;
        }

        String password = args[0];

        UUID uuid = player.getUniqueId();
        AtomicInteger count = new AtomicInteger(plugin.getConfig().getInt("login.chances"));
        
        plugin.getDatabase().playerExists(uuid).thenAccept(exists -> Bukkit.getScheduler().runTask(plugin, () -> {
            if (exists) {
                plugin.getDatabase().isPassword(uuid, password).thenAccept(isPassword -> Bukkit.getScheduler().runTask(plugin, () -> {
                    if (!isPassword) {
                        count.decrementAndGet();

                        player.sendActionBar("$c Invalid Password, You have only " 
                            + count.get() + " Chances");
                        return;
                    }

                    player.sendMessage("§a Logged With Success!");
                    return;
                }));
            return;
        }
            player.sendMessage("You are not registered.");
        }));
        
        return true;
    }
}

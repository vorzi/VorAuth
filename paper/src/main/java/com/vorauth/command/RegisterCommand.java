package com.vorauth.command;

import com.vorauth.Main;
import com.vorauth.session.HashCrypt;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
    private final Main plugin;

    public RegisterCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage("Usage: /register <password> <password>");
            return true;
        }

        String password1 = args[0];
        String password2 = args[1];

        if (!password1.equals(password2)) {
            player.sendMessage("Password isnt valid, please try a better password or the same password");
            return true;
        }

        if (!password1.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,64}$")) {
            player.sendMessage("Weak password! Use uppercase, lowercase, number and special character.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        plugin.getDatabase().playerExists(uuid).thenAccept(exists -> Bukkit.getScheduler().runTask(plugin, () -> {
            if (exists) {
                player.sendMessage("You are already registered.");
                return;
            }

            int roundSalt = plugin.getConfig().getInt("hash.roundSalt");
            String hashedPassword = HashCrypt.hash(password1, roundSalt);

            plugin.getDatabase().createUser(uuid, hashedPassword).thenAccept(created -> Bukkit.getScheduler().runTask(plugin, () -> {
                if (created) {
                    player.sendMessage("Registered successfully! Try to login");
                } else {
                    player.sendMessage("Failed to register (try see the console). Try again later.");
                }
            }));
        }));
        
        return true;
    }
}

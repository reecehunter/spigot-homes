package net.bosshub.homes.commands;

import net.bosshub.homes.HomesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public class DeleteHomeCommand implements CommandExecutor {

    private final HomesPlugin plugin;

    public DeleteHomeCommand(HomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;


            if (player.hasPermission("homes.use")) {

                if (args.length != 1) {
                    player.sendMessage(ChatColor.RED + "Usage: /delhome [name]");
                    return false;
                }

                UUID uuid = player.getUniqueId();

                // TODO: Check if home name is valid
                Set<String> homeNames = plugin.getFiles().getAllHomeNames(uuid);

                if(homeNames.contains(args[0])) {
                    plugin.getFiles().removeHome(uuid, args[0]);
                    player.sendMessage(ChatColor.GREEN + "Your home was deleted.");
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "That is an invalid home name.");
                }

            }

        }

        return true;

    }

}

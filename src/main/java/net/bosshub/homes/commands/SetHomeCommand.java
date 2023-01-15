package net.bosshub.homes.commands;

import net.bosshub.homes.HomesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SetHomeCommand implements CommandExecutor {

    private final HomesPlugin plugin;

    private final ChatColor ccGood = ChatColor.GREEN;
    private final ChatColor ccBad = ChatColor.RED;

    public SetHomeCommand(HomesPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("homes.use")) {

                UUID uuid = player.getUniqueId();

                int homeLimit = 0;
                for (int i = 1; i <= 20; i++) {
                    if (player.hasPermission("homes.limit." + i)) {
                        if (homeLimit < i) {
                            homeLimit = i;
                        }
                    }
                }

                int homeAmount = plugin.getFiles().getAllHomeNames(uuid).size();
                if(homeAmount >= homeLimit) {

                    player.sendMessage(ChatColor.RED + "You have reached your home limit. " + ChatColor.YELLOW + "/buy " + ChatColor.RED + "to get more.");

                } else {

                    if (args.length != 1) {
                        player.sendMessage(ChatColor.RED + "Usage: /sethome [name]");
                        return false;
                    }

                    Location location = player.getLocation();

                    player.sendMessage(ccGood + "You set a new home.");

                    plugin.getFiles().addHome(uuid, location, args[0]);

                    plugin.addHome(uuid, location, args[0]);
                }

            } else {
                player.sendMessage(ccBad + "You do not have permission for that.");
            }

        }

        return true;
    }
}

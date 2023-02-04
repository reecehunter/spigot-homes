package net.bosshub.homes.commands;

import net.bosshub.homes.HomesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HomeCommand implements CommandExecutor {

    private final HomesPlugin plugin;

    private final ChatColor ccGood = ChatColor.GREEN;
    private final ChatColor ccBad = ChatColor.RED;

    public HomeCommand(HomesPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("homes.use")) {

                if (args.length == 1) {

                    UUID uuid = player.getUniqueId();

                    if (!plugin.hasHome(uuid)) {
                        player.sendMessage(ccBad + "You do not have any homes! Set one with /sethome");
                    } else {

                        if(plugin.getFiles().getAllHomeNames(uuid).contains(args[0])) {

                            plugin.addQueue(uuid);

                            new BukkitRunnable() {
                                int delay = 5;

                                @Override
                                public void run() {
                                    if (plugin.isQueued(uuid)) {
                                        if (delay <= 0) {
                                            player.teleport(plugin.getFiles().getHome(uuid, args[0]));
                                            player.sendMessage(ccGood + "You teleported to your home.");
                                            plugin.cancelQueue(uuid);
                                            this.cancel();
                                        } else {
                                            player.sendMessage(ccGood + "Teleport home in " + delay-- + " seconds.");
                                        }
                                    } else {
                                        player.sendMessage(ccBad + "Your teleport has been cancelled.");
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0, 20);

                        } else {
                            player.sendMessage(ccBad + "That home doesn't exist.");
                        }


                    }
                } else {
                    player.sendMessage(ccBad + "Usage: /home [name]");
                }

            } else {
                player.sendMessage(ccBad + "You do not have permission for that.");
            }

        }

        return true;
    }
}

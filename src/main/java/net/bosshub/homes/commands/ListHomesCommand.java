package net.bosshub.homes.commands;

import net.bosshub.homes.HomesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ListHomesCommand implements CommandExecutor, Listener {

    private final HomesPlugin plugin;

    public ListHomesCommand(HomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(player.hasPermission("homes.use")) {

                UUID uuid = player.getUniqueId();


                if(plugin.getFiles().getAllHomeNames(uuid) == null) {
                    player.sendMessage(ChatColor.RED + "You don't have any homes.");
                } else {
                    plugin.getHomeListInventory().initializeItems(uuid);
                    plugin.getHomeListInventory().openInventory(player);
                }



            }

        }

        return true;

    }

}

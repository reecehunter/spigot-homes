package net.bosshub.homes.listeners;

import net.bosshub.homes.HomesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerListeners implements Listener {

    private final HomesPlugin plugin;
    public PlayerListeners(HomesPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            if(plugin.isQueued(uuid)) {
                plugin.cancelQueue(uuid);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID uuid = player.getUniqueId();

            if(plugin.isQueued(uuid)) {
                plugin.cancelQueue(uuid);
            }
        }
    }

}

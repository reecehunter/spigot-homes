package net.bosshub.homes.inventories;

import net.bosshub.homes.HomesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class HomeListInventory implements Listener {

    private final HomesPlugin plugin;

    public Inventory inv;
    private String invName = "                 Homes";

    public HomeListInventory(HomesPlugin plugin) {
        // Get main class
        this.plugin = plugin;

        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 27, invName);
    }

    public void initializeItems(UUID uuid) {
        inv.clear();
        Set<String> homeNames = plugin.getFiles().getAllHomeNames(uuid);

        for(String name : homeNames) {
            inv.addItem(createGuiItem(Material.LIME_DYE , "§a§n§l"+name, "§fLeft click to teleport"));
        }

        int homeLimit = 0;
        for (int i = 1; i <= 20; i++) {
            if (Bukkit.getPlayer(uuid).hasPermission("homes.limit." + i)) {
                if (homeLimit < i) {
                    homeLimit = i;
                }
            }
        }

        int homes = plugin.getFiles().getAllHomeNames(uuid).size();
        int homeDifference = homeLimit - homes;

        if(homeDifference > 0) {
            for(int i = 0; i < homeDifference; i++) {
                inv.setItem(homes + i, createGuiItem(Material.RED_DYE, "§c§lEmpty Slot", "§fUse /sethome"));
            }
        }
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity e) {
        e.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(!e.getView().getTitle().equals(invName)) return;

        e.setCancelled(true);

        if (e.getClick() == ClickType.SWAP_OFFHAND) return;

        final Player player = (Player) e.getWhoClicked();

        if(e.getClickedInventory() instanceof PlayerInventory) return;

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        // Teleport the player
        String itemName = clickedItem.getItemMeta().getDisplayName();
        if(itemName.contains("§a§l§n")) {
            player.performCommand("home " + itemName.replace("§a§l§n", ""));
            player.closeInventory();
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}

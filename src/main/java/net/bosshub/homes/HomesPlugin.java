package net.bosshub.homes;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.bosshub.homes.commands.DeleteHomeCommand;
import net.bosshub.homes.commands.HomeCommand;
import net.bosshub.homes.commands.ListHomesCommand;
import net.bosshub.homes.commands.SetHomeCommand;
import net.bosshub.homes.inventories.HomeListInventory;
import net.bosshub.homes.listeners.PlayerListeners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class HomesPlugin extends JavaPlugin {

    private Multimap<UUID, HashMap<String, Location>> homes;
    private List<UUID> queue;
    private HomeFiles files;
    private HomeListInventory homeListInventory;


    @Override
    public void onEnable() {
        this.homes = ArrayListMultimap.create();
        this.files = new HomeFiles(this);
        this.homeListInventory = new HomeListInventory(this);
        this.queue = new ArrayList<>();
        this.files.init();

        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // Set up config.yml
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Set up commands
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("homes").setExecutor(new ListHomesCommand(this));
        getCommand("removehome").setExecutor(new DeleteHomeCommand(this));

        // Set up listeners
        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
        getServer().getPluginManager().registerEvents(new HomeListInventory(this), this);
    }

    @Override
    public void onDisable() {
        this.files.saveAllHomes();
    }

    public void addHome(UUID uuid, Location location, String name) {
        HashMap<String, Location> info = new HashMap<>();
        info.put(name, location);
        this.homes.put(uuid, info);
    }

    public Location getHome(UUID uuid, String name) {
        // TODO: Get the specific value (from the multimap array) by its name
        Bukkit.getLogger().info("[GETHOME DEBUG]: " + this.homes.get(uuid));
        return (Location) this.homes.get(uuid);
    }

    public boolean hasHome(UUID uuid) {
        return this.homes.containsKey(uuid);
    }

    public Multimap<UUID, HashMap<String, Location>> getHomes() {
        return homes;
    }

    public HomeFiles getFiles() {
        return files;
    }

    public HomeListInventory getHomeListInventory() {
        return homeListInventory;
    }

    public void addQueue(UUID uuid) {
        this.queue.add(uuid);
    }

    public void cancelQueue(UUID uuid) {
        this.queue.remove(uuid);
    }

    public boolean isQueued(UUID uuid) {
        return this.queue.contains(uuid);
    }
}

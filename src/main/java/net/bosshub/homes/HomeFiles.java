package net.bosshub.homes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class HomeFiles {
    private final HomesPlugin plugin;
    public HomeFiles(HomesPlugin plugin) {
        this.plugin = plugin;
    }


    public void init() {
        File homeFile = new File(plugin.getDataFolder(), "homes.yml");

        if(homeFile.exists()) {
            YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

            for(String s : homeConfig.getKeys(false)) {
                UUID uuid = UUID.fromString(s);
                Location location = homeConfig.getLocation(s);
                String name = homeConfig.getString(s);
                plugin.addHome(uuid, location, name);
            }
        }
    }


    public void saveAllHomes() {
        File homeFile = new File(plugin.getDataFolder(), "homes.yml");

        if(!homeFile.exists()) {
            try {
                homeFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

        for(UUID uuid : plugin.getHomes().keySet()) {
            for(String name : homeConfig.getConfigurationSection(uuid.toString()).getKeys(false)) {
                homeConfig.set(uuid.toString(), plugin.getHome(uuid, name));
            }
        }

        try {
            homeConfig.save(homeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addHome(UUID uuid, Location location, String name) {
        File homeFile = new File(plugin.getDataFolder(), "homes.yml");

        if(!homeFile.exists()) {
            try {
                homeFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);
        homeConfig.set(uuid.toString() + "." + name, location);

        try {
            homeConfig.save(homeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getHome(UUID uuid, String name) {
        File homeFile = new File(plugin.getDataFolder(), "homes.yml");

        if(!homeFile.exists()) {
            try {
                homeFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

        Bukkit.getLogger().info("HOMEFILES: " + homeConfig.getConfigurationSection(uuid.toString()).get(name));
        return (Location) homeConfig.getConfigurationSection(uuid.toString()).get(name);

    }

    public Set<String> getAllHomeNames(UUID uuid) {
        File homeFile = new File(plugin.getDataFolder(), "homes.yml");

        if(!homeFile.exists()) {
            try {
                homeFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

        return homeConfig.getConfigurationSection(uuid.toString()).getKeys(false);

    }

    public void removeHome(UUID uuid, String name) {
        File homeFile = new File(plugin.getDataFolder(), "homes.yml");

        if(!homeFile.exists()) {
            try {
                homeFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);
        homeConfig.set(uuid.toString() + "." + name, null);

        try {
            homeConfig.save(homeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

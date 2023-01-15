package net.bosshub.homes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeFiles {

    HomeFiles plugin;
    File homeFile;

    public TestConfig(HomeFiles plugin, String name) {
        this.plugin = plugin;
        
        // Specify the file name in the main class
        homeFile = new File(plugin.getDataFolder(), name);
        
        // Example HomesPlugin.class:
        
        // HomeFiles homeFiles = new HomeFiles(this, "homes.yml");
        // homeFiles.init();
    }

    public void init() {

        Preconditions.checkArgument(homeFile.exists(), "Configuration '%s' can`t be null", homeFile.getName());
        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

        for (String s : homeConfig.getKeys(false))
            plugin.addHome(UUID.fromString(s), homeConfig.getLocation(s), homeConfig.getString(s));
    }

    @SneakyThrows
    public void saveAllHomes() {

        if (!homeFile.exists()) 
            homeFile.createNewFile();

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

        for (UUID uuid : plugin.getHomes().keySet()) {
            for (String name : homeConfig.getConfigurationSection(uuid.toString()).getKeys(false))
                homeConfig.set(uuid.toString(), plugin.getHome(uuid, name));
        }

        homeConfig.save(homeFile);
    }


    @SneakyThrows
    public void addHome(UUID uuid, Location location, String name) {

        if (!homeFile.exists()) 
            homeFile.createNewFile();

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);
        homeConfig.set(uuid.toString() + "." + name, location);
        homeConfig.save(homeFile);
    }

    @SneakyThrows
    public Location getHome(UUID uuid, String name) {

        if (!homeFile.exists()) 
            homeFile.createNewFile();

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

        Bukkit.getLogger().info("HOMEFILES: " + homeConfig.getConfigurationSection(uuid.toString()).get(name));
        return (Location) homeConfig.getConfigurationSection(uuid.toString()).get(name);

    }

    @SneakyThrows
    public Set<String> getAllHomeNames(UUID uuid) {

        if (!homeFile.exists()) 
            homeFile.createNewFile();

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);
        return homeConfig.getConfigurationSection(uuid.toString()).getKeys(false);

    }

    @SneakyThrows
    public void removeHome(UUID uuid, String name) {

        if (!homeFile.exists()) 
            homeFile.createNewFile();

        YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);
        homeConfig.set(uuid.toString() + "." + name, null);
        homeConfig.save(homeFile);
    }
}

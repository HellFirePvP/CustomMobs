package de.hellfirepvp.lib;

import java.util.ArrayList;
import java.io.IOException;
import de.hellfirepvp.CustomMobs;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public class LibConfiguration
{
    private static File aiDirectory;
    private static File configFile;
    private static File spawnConfigFile;
    private static File spawnerDataFile;
    private static File respawnSettingsFile;
    private static File stackSettingsFile;
    private static File leashDataFile;
    private static File fullControlBiomeFile;
    
    public static YamlConfiguration getLeashDataConfiguration() {
        return loadYaml(getLeashDataFile(), LibConfiguration.leashDataFile);
    }
    
    public static YamlConfiguration getStackDataConfiguration() {
        return loadYaml(getStackSettingsFile(), LibConfiguration.stackSettingsFile);
    }
    
    public static YamlConfiguration getSpawnerDataConfiguration() {
        return loadYaml(getSpawnerDataFile(), LibConfiguration.spawnerDataFile);
    }
    
    public static YamlConfiguration getRespawnSettingsConfiguration() {
        return loadYaml(getRespawnSettingsFile(), LibConfiguration.respawnSettingsFile);
    }
    
    public static YamlConfiguration getSpawnSettingsConfiguration() {
        return loadYaml(getSpawnSettingsFile(), LibConfiguration.spawnConfigFile);
    }
    
    public static YamlConfiguration getFullcontrolBiomeConfiguration() {
        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(getFullControlBiomeFile());
        if (!LibConfiguration.fullControlBiomeFile.exists()) {
            CustomMobs.instance.getFullControlHandler().pushDefaultData(cfg);
            try {
                cfg.save(LibConfiguration.fullControlBiomeFile);
            }
            catch (IOException ex) {}
        }
        return cfg;
    }
    
    public static YamlConfiguration getConfigSettingsConfiguration() {
        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(getConfigFile());
        if (!LibConfiguration.configFile.exists()) {
            cfg.set("config-version", (Object)CustomMobs.pluginYmlVersion);
            cfg.set("spawnAtStartup", (Object)Boolean.TRUE);
            cfg.set("enablePluginMetrics", (Object)true);
            cfg.set("debug", (Object)false);
            cfg.set("languageFile", (Object)"en_US");
            cfg.set("worldSpawnerTickSpeed", (Object)5);
            cfg.set("spawnAtStartupDelay", (Object)100);
            cfg.set("worldSpawnFrequency", (Object)10);
            cfg.set("worldSpawnThreshold", (Object)256);
            cfg.set("spawnerRange", (Object)16);
            cfg.set("useFullControl", (Object)false);
            cfg.set("useFullControl", (Object)false);
            cfg.set("removeLimitedMobsOnChunkUnload", (Object)true);
            cfg.set("removeCustomMobsOnChunkUnload", (Object)true);
            cfg.set("leashViolationTolerance", (Object)100);
            cfg.set("respectWorldGuardDenySpawnFlag", (Object)true);
            cfg.set("respectWorldGuardMobSpawningFlag", (Object)true);
            cfg.set("bannedMobCommands", (Object)new ArrayList<String>() {
                {
                    this.add("pex");
                    this.add("restart");
                    this.add("stop");
                    this.add("reload");
                    this.add("op");
                    this.add("sudo");
                }
            });
            cfg.set("spawnlimitResetCommands", (Object)new ArrayList<String>() {
                {
                    this.add("butcher");
                    this.add("killall");
                }
            });
            try {
                cfg.save(LibConfiguration.configFile);
            }
            catch (IOException ex) {}
        }
        return cfg;
    }
    
    private static boolean afterwardChange(final YamlConfiguration cfg, final String key, final Object defaultValue) {
        if (!cfg.contains(key)) {
            cfg.set(key, defaultValue);
            return true;
        }
        return false;
    }
    
    public static File getStackSettingsFile() {
        return LibConfiguration.stackSettingsFile = loadFile(LibConfiguration.stackSettingsFile, "stackSettings.yml");
    }
    
    public static File getAiDirectory() {
        return LibConfiguration.aiDirectory = loadDirectory(LibConfiguration.aiDirectory, "ai");
    }
    
    public static File getLeashDataFile() {
        return LibConfiguration.leashDataFile = loadFile(LibConfiguration.leashDataFile, getAiDirectory(), "leashData.yml");
    }
    
    public static File getSpawnerDataFile() {
        return LibConfiguration.spawnerDataFile = loadFile(LibConfiguration.spawnerDataFile, "spawnerData.yml");
    }
    
    public static File getConfigFile() {
        return LibConfiguration.configFile = loadFile(LibConfiguration.configFile, "config.yml");
    }
    
    public static File getFullControlBiomeFile() {
        return LibConfiguration.fullControlBiomeFile = loadFile(LibConfiguration.fullControlBiomeFile, CustomMobs.instance.getFullControlDataFolder(), "fullcontrolBiomes.yml");
    }
    
    public static File getSpawnSettingsFile() {
        return LibConfiguration.spawnConfigFile = loadFile(LibConfiguration.spawnConfigFile, "spawnSettings.yml");
    }
    
    public static File getRespawnSettingsFile() {
        return LibConfiguration.respawnSettingsFile = loadFile(LibConfiguration.respawnSettingsFile, "respawnSettings.yml");
    }
    
    private static File loadDirectory(File store, final File parentDirectory, final String name) {
        if (store == null) {
            store = new File(parentDirectory, name);
        }
        if (!store.exists()) {
            store.mkdirs();
        }
        return store;
    }
    
    private static File loadDirectory(final File store, final String name) {
        return loadDirectory(store, CustomMobs.instance.getDataFolder(), name);
    }
    
    private static File loadFile(File store, final File directory, final String name) {
        if (store == null) {
            store = new File(directory, name);
        }
        return store;
    }
    
    private static File loadFile(final File store, final String name) {
        return loadFile(store, CustomMobs.instance.getDataFolder(), name);
    }
    
    private static YamlConfiguration loadYaml(final File f, final File store) {
        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        if (!store.exists()) {
            try {
                cfg.save(store);
            }
            catch (IOException ex) {}
        }
        return cfg;
    }
    
    static {
        LibConfiguration.aiDirectory = null;
        LibConfiguration.configFile = null;
        LibConfiguration.spawnConfigFile = null;
        LibConfiguration.spawnerDataFile = null;
        LibConfiguration.respawnSettingsFile = null;
        LibConfiguration.stackSettingsFile = null;
        LibConfiguration.leashDataFile = null;
        LibConfiguration.fullControlBiomeFile = null;
    }
}

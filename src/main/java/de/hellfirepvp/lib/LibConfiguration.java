package de.hellfirepvp.lib;

import de.hellfirepvp.CustomMobs;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static de.hellfirepvp.lib.LibMisc.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: LibConfiguration
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:02
 */
public class LibConfiguration {

    private static File aiDirectory = null;

    private static File configFile = null;
    private static File spawnConfigFile = null;
    private static File spawnerDataFile = null;
    private static File respawnSettingsFile = null;

    private static File leashDataFile = null;

    private static File fullControlBiomeFile = null;

    public static YamlConfiguration getLeashDataConfiguration() {
        return loadYaml(getLeashDataFile(), leashDataFile);
    }

    public static YamlConfiguration getSpawnerDataConfiguration() {
        return loadYaml(getSpawnerDataFile(), spawnerDataFile);
    }

    public static YamlConfiguration getRespawnSettingsConfiguration() {
        return loadYaml(getRespawnSettingsFile(), respawnSettingsFile);
    }

    public static YamlConfiguration getSpawnSettingsConfiguration() {
        return loadYaml(getSpawnSettingsFile(), spawnConfigFile);
    }

    public static YamlConfiguration getFullcontrolBiomeConfiguration() {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(getFullControlBiomeFile());
        if(!fullControlBiomeFile.exists()) {
            CustomMobs.instance.getFullControlHandler().pushDefaultData(cfg);
            try {
                cfg.save(fullControlBiomeFile);
            } catch (IOException ignored) {}
        }
        return cfg;
    }

    public static YamlConfiguration getConfigSettingsConfiguration() {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(getConfigFile());
        if(!configFile.exists()) {
            cfg.set("config-version", CustomMobs.pluginYmlVersion);
            cfg.set(LibConstantKeys.CONFIG_DATA_SPAWN_AT_STARTUP_BOOL, Boolean.TRUE);
            cfg.set(LibConstantKeys.CONFIG_DATA_ENABLE_PLMETRICS, true); //*cough*
            cfg.set(LibConstantKeys.CONFIG_DATA_DEBUG, false);
            cfg.set(LibConstantKeys.CONFIG_DATA_LANG_FILE, "en_US");
            cfg.set(LibConstantKeys.CONFIG_DATA_WORLD_SPAWNER_TICKSPEED, 5);
            cfg.set(LibConstantKeys.CONFIG_DATA_SPAWN_AT_STARTUP_DELAY, 100);
            cfg.set(LibConstantKeys.CONFIG_DATA_WS_FREQUENCY, 10);
            cfg.set(LibConstantKeys.CONFIG_DATA_WS_THRESHOLD, 256);
            cfg.set(LibConstantKeys.CONFIG_DATA_SPAWNER_RANGE, 16);
            cfg.set(LibConstantKeys.CONFIG_DATA_USE_FULLCONTROL, false);
            cfg.set(LibConstantKeys.CONFIG_DATA_USE_FULLCONTROL, false);
            cfg.set(LibConstantKeys.CONFIG_DATA_REMOVE_LIMITED_MOBS_ON_CHUNK_UNLOAD, true);
            cfg.set(LibConstantKeys.CONFIG_DATA_REMOVE_CUSTOMMOBS_ON_CHUNK_UNLOAD, true);
            cfg.set(LibConstantKeys.CONFIG_DATA_LEASH_VIOLATION_TOLERANCE, 100);
            cfg.set(LibConstantKeys.CONFIG_DATA_RESPECT_DENY_FLAG, true);
            cfg.set(LibConstantKeys.CONFIG_DATA_RESPECT_TYPES_FLAG, true);
            cfg.set(LibConstantKeys.CONFIG_DATA_BANNED_MOB_COMMANDS, new ArrayList<String>() {
                {
                    add("pex");
                    add("restart");
                    add("stop");
                    add("reload");
                    add("op");
                    add("sudo");
                }
            });
            cfg.set(LibConstantKeys.CONFIG_DATA_SPAWNLIMIT_RESET_COMMANDS, new ArrayList<String>() {
                {
                    add("butcher");
                    add("killall");
                }
            });

            try {
                cfg.save(configFile);
            } catch (IOException ignored) {}
        }

        return cfg;
    }

    private static boolean afterwardChange(YamlConfiguration cfg, String key, Object defaultValue) {
        if(!cfg.contains(key)) {
            cfg.set(key, defaultValue);
            return true;
        }
        return false;
    }

    public static File getAiDirectory() {
        aiDirectory = loadDirectory(aiDirectory, DIRECTORY_AI);
        return aiDirectory;
    }

    public static File getLeashDataFile() {
        leashDataFile = loadFile(leashDataFile, getAiDirectory(), FILENAME_AI_LEASH);
        return leashDataFile;
    }

    public static File getSpawnerDataFile() {
        spawnerDataFile = loadFile(spawnerDataFile, FILENAME_SPAWNERDATA);
        return spawnerDataFile;
    }

    public static File getConfigFile() {
        configFile = loadFile(configFile, FILENAME_CONFIG);
        return configFile;
    }

    public static File getFullControlBiomeFile() {
        fullControlBiomeFile = loadFile(fullControlBiomeFile, CustomMobs.instance.getFullControlDataFolder(), FILENAME_FULLCONTROL_BIOMES);
        return fullControlBiomeFile;
    }

    public static File getSpawnSettingsFile() {
        spawnConfigFile = loadFile(spawnConfigFile, FILENAME_SPAWNSETTINGS);
        return spawnConfigFile;
    }

    public static File getRespawnSettingsFile() {
        respawnSettingsFile = loadFile(respawnSettingsFile, FILENAME_RESPAWNSETTINGS);
        return respawnSettingsFile;
    }

    //
    // Acutal loading
    //

    private static File loadDirectory(File store, File parentDirectory, String name) {
        if(store == null) {
            store = new File(parentDirectory, name);
        }
        if(!store.exists()) {
            store.mkdirs();
        }
        return store;
    }

    private static File loadDirectory(File store, String name) {
        return loadDirectory(store, CustomMobs.instance.getDataFolder(), name);
    }

    private static File loadFile(File store, File directory, String name) {
        if(store == null) store = new File(directory, name);
        return store;
    }

    private static File loadFile(File store, String name) {
        return loadFile(store, CustomMobs.instance.getDataFolder(), name);
    }

    private static YamlConfiguration loadYaml(File f, File store) {
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        if(!store.exists()) {
            try {
                cfg.save(store);
            } catch (IOException ignored) {}
        }
        return cfg;
    }

}

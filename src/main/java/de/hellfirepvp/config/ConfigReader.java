package de.hellfirepvp.config;

import de.hellfirepvp.lib.LibConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.stream.Collectors;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ConfigReader
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class ConfigReader {

    public static void readAndUpdateConfig(ConfigHandler out) {
        YamlConfiguration config = LibConfiguration.getConfigSettingsConfiguration();

        int frequency = Math.min(100, Math.max(0, config.getInt(CONFIG_DATA_WS_FREQUENCY, 10)));
        int threshold = Math.min(4096, Math.max(16, config.getInt(CONFIG_DATA_WS_THRESHOLD, 256)));
        boolean spawnAtStartup = config.getBoolean(CONFIG_DATA_SPAWN_AT_STARTUP_BOOL, false);
        boolean enableMetrics = config.getBoolean(CONFIG_DATA_ENABLE_PLMETRICS, true);
        boolean debug = config.getBoolean(CONFIG_DATA_DEBUG, false);
        int spawnAtStartupDelay = Math.max(0, config.getInt(CONFIG_DATA_SPAWN_AT_STARTUP_DELAY, 100));
        int spawnerRange = config.getInt(CONFIG_DATA_SPAWNER_RANGE, 16);
        int leashTolerance = config.getInt(CONFIG_DATA_LEASH_VIOLATION_TOLERANCE, 100);
        int worldSpawnerTickSpeed = config.getInt(CONFIG_DATA_WORLD_SPAWNER_TICKSPEED, 5);
        boolean fullcontrolUsage = config.getBoolean(CONFIG_DATA_USE_FULLCONTROL, false);
        boolean removeOnChUnload = config.getBoolean(CONFIG_DATA_REMOVE_LIMITED_MOBS_ON_CHUNK_UNLOAD, true);
        boolean removeCMonChUnload = config.getBoolean(CONFIG_DATA_REMOVE_CUSTOMMOBS_ON_CHUNK_UNLOAD, true);
        boolean respectDenyFlag = config.getBoolean(CONFIG_DATA_RESPECT_DENY_FLAG, true);
        boolean respectTypesFlag = config.getBoolean(CONFIG_DATA_RESPECT_TYPES_FLAG, true);
        String langFileName = config.getString(CONFIG_DATA_LANG_FILE, "en_US");
        List<String> bannedCommands = config.getStringList(CONFIG_DATA_BANNED_MOB_COMMANDS);
        List<String> spawnLimitResetCommands = config.getStringList(CONFIG_DATA_SPAWNLIMIT_RESET_COMMANDS);
        List<String> resetCommandsWithSlash = spawnLimitResetCommands.stream().map(reset -> "/" + reset).collect(Collectors.toList());

        out.frequency = frequency;
        out.debug = debug;
        out.spawnThreshold = threshold;
        out.languageFile = langFileName;
        out.spawnAtStartup = spawnAtStartup;
        out.enableMetrics = enableMetrics;
        out.removeLimitedMobsAtChUnload = removeOnChUnload;
        out.removeCustomMobsOnChunkUnload = removeCMonChUnload;
        out.respectWGDenySpawnFlag = respectDenyFlag;
        out.respectWGMobSpawningTypesFlag = respectTypesFlag;
        out.spawnerRange = spawnerRange;
        out.worldSpawnerTickSpeed = worldSpawnerTickSpeed;
        out.leashViolationTolerance = leashTolerance;
        out.spawnAtStartupDelay = spawnAtStartupDelay;
        out.bannedMobCommands = bannedCommands;
        out.spawnLimitResetCommands = resetCommandsWithSlash;
        out.fullcontrolUsage = fullcontrolUsage;
    }

}

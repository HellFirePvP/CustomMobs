package de.hellfirepvp.config;

import de.hellfirepvp.CustomMobs;

import java.util.Arrays;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ConfigHandler
 * Created by HellFirePvP
 * Date: 23.05.2016 / 23:34
 */
public class ConfigHandler {

    protected int frequency, spawnAtStartupDelay, spawnerRange,
            leashViolationTolerance, worldSpawnerTickSpeed, spawnThreshold;
    protected boolean spawnAtStartup, fullcontrolUsage, enableMetrics,
            removeLimitedMobsAtChUnload, removeCustomMobsOnChunkUnload,
            respectWGDenySpawnFlag, respectWGMobSpawningTypesFlag, debug;
    protected List<String> bannedMobCommands, spawnLimitResetCommands;
    protected String languageFile;

    public void loadFromFile() {
        ConfigReader.readAndUpdateConfig(this);

        CustomMobs.logger.info("Loaded configuration!");
        CustomMobs.logger.debug("LanguageFile: " + languageFile);
        CustomMobs.logger.debug("Debug: " + debug);
        CustomMobs.logger.debug("SpawnThreshold: " + spawnThreshold);
        CustomMobs.logger.debug("SpawnFrequency: " + frequency + "/100");
        CustomMobs.logger.debug("WorldSpawnerTickSpeed: " + worldSpawnerTickSpeed);
        CustomMobs.logger.debug("SpawnAtStartup: " + spawnAtStartup);
        CustomMobs.logger.debug("SpawnAtStartupDelay: " + spawnAtStartupDelay);
        CustomMobs.logger.debug("SpawnerRange: " + spawnerRange);
        CustomMobs.logger.debug("EnablePluginMetrics: " + enableMetrics);
        CustomMobs.logger.debug("LeashViolationTolerance: " + leashViolationTolerance);
        CustomMobs.logger.debug("UseFullcontrol: " + fullcontrolUsage);
        CustomMobs.logger.debug("RespectWGDenySpawn: " + respectWGDenySpawnFlag);
        CustomMobs.logger.debug("RespectWGMobSpawningTypes: " + respectWGMobSpawningTypesFlag);
        CustomMobs.logger.debug("RemoveCustomMobsOnChunkUnload: " + removeCustomMobsOnChunkUnload);
        CustomMobs.logger.debug("RemoveLimitedMobsOnChunkUnload: " + removeLimitedMobsAtChUnload);
        CustomMobs.logger.debug("BannedMobCommands: " + Arrays.toString(bannedMobCommands.toArray()));
        CustomMobs.logger.debug("SpawnlimitResetCommand: " + Arrays.toString(spawnLimitResetCommands.toArray()));
    }

    public boolean debug() {
        return debug;
    }

    public boolean useFullControl() {
        return fullcontrolUsage;
    }

    public int spawnThreshold() {
        return spawnThreshold;
    }

    public boolean spawnRespawnMobsAtStartup() {
        return spawnAtStartup;
    }

    public boolean removeLimitedMobsOnChunkUnload() {
        return removeLimitedMobsAtChUnload;
    }

    public boolean removeCustomMobOnChunkUnload() {
        return removeCustomMobsOnChunkUnload;
    }

    public boolean respectWGMobSpawningFlag() {
        return respectWGMobSpawningTypesFlag;
    }

    public boolean respectWGDenySpawnFlag() {
        return respectWGDenySpawnFlag;
    }

    public boolean enablePluginMetrics() {
        return enableMetrics;
    }

    public int spawnRespawnMobsAtStartupDelay() {
        return spawnAtStartupDelay;
    }

    public int spawnFrequency() {
        return frequency;
    }

    public String getLanguageFileConfiguration() {
        return languageFile;
    }

    public int worldSpawnerTickSpeed() {
        return worldSpawnerTickSpeed;
    }

    public int spawnerRange() {
        return spawnerRange;
    }

    public int leashViolationTolerance() {
        return leashViolationTolerance;
    }

    public List<String> bannedMobCommands() {
        return bannedMobCommands;
    }

    public List<String> spawnLimitFlushCommands() {
        return spawnLimitResetCommands;
    }
}

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
            respectWGDenySpawnFlag, respectWGMobSpawningTypesFlag;
    protected List<String> bannedMobCommands, spawnLimitResetCommands;
    protected String languageFile;

    public void loadFromFile() {
        ConfigReader.readAndUpdateConfig(this);

        CustomMobs.logger.info("Loaded configuration!");
        CustomMobs.logger.info("LanguageFile: " + languageFile);
        CustomMobs.logger.info("SpawnThreshold: " + spawnThreshold);
        CustomMobs.logger.info("SpawnFrequency: " + frequency + "/100");
        CustomMobs.logger.info("WorldSpawnerTickSpeed: " + worldSpawnerTickSpeed);
        CustomMobs.logger.info("SpawnAtStartup: " + spawnAtStartup);
        CustomMobs.logger.info("SpawnAtStartupDelay: " + spawnAtStartupDelay);
        CustomMobs.logger.info("SpawnerRange: " + spawnerRange);
        CustomMobs.logger.info("EnablePluginMetrics: " + enableMetrics);
        CustomMobs.logger.info("LeashViolationTolerance: " + leashViolationTolerance);
        CustomMobs.logger.info("UseFullcontrol: " + fullcontrolUsage);
        CustomMobs.logger.info("RespectWGDenySpawn: " + respectWGDenySpawnFlag);
        CustomMobs.logger.info("RespectWGMobSpawningTypes: " + respectWGMobSpawningTypesFlag);
        CustomMobs.logger.info("RemoveCustomMobsOnChunkUnload: " + removeCustomMobsOnChunkUnload);
        CustomMobs.logger.info("RemoveLimitedMobsOnChunkUnload: " + removeLimitedMobsAtChUnload);
        CustomMobs.logger.info("BannedMobCommands: " + Arrays.toString(bannedMobCommands.toArray()));
        CustomMobs.logger.info("SpawnlimitResetCommand: " + Arrays.toString(spawnLimitResetCommands.toArray()));
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

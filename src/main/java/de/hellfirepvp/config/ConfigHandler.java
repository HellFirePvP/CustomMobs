package de.hellfirepvp.config;

import java.util.Arrays;
import de.hellfirepvp.CustomMobs;
import java.util.List;

public class ConfigHandler
{
    protected int frequency;
    protected int spawnAtStartupDelay;
    protected int spawnerRange;
    protected int leashViolationTolerance;
    protected int worldSpawnerTickSpeed;
    protected int spawnThreshold;
    protected boolean spawnAtStartup;
    protected boolean fullcontrolUsage;
    protected boolean enableMetrics;
    protected boolean removeLimitedMobsAtChUnload;
    protected boolean removeCustomMobsOnChunkUnload;
    protected boolean respectWGDenySpawnFlag;
    protected boolean respectWGMobSpawningTypesFlag;
    protected boolean debug;
    protected List<String> bannedMobCommands;
    protected List<String> spawnLimitResetCommands;
    protected String languageFile;
    
    public void loadFromFile() {
        ConfigReader.readAndUpdateConfig(this);
        CustomMobs.logger.info("Loaded configuration!");
        CustomMobs.logger.debug("LanguageFile: " + this.languageFile);
        CustomMobs.logger.debug("Debug: " + this.debug);
        CustomMobs.logger.debug("SpawnThreshold: " + this.spawnThreshold);
        CustomMobs.logger.debug("SpawnFrequency: " + this.frequency + "/100");
        CustomMobs.logger.debug("WorldSpawnerTickSpeed: " + this.worldSpawnerTickSpeed);
        CustomMobs.logger.debug("SpawnAtStartup: " + this.spawnAtStartup);
        CustomMobs.logger.debug("SpawnAtStartupDelay: " + this.spawnAtStartupDelay);
        CustomMobs.logger.debug("SpawnerRange: " + this.spawnerRange);
        CustomMobs.logger.debug("EnablePluginMetrics: " + this.enableMetrics);
        CustomMobs.logger.debug("LeashViolationTolerance: " + this.leashViolationTolerance);
        CustomMobs.logger.debug("UseFullcontrol: " + this.fullcontrolUsage);
        CustomMobs.logger.debug("RespectWGDenySpawn: " + this.respectWGDenySpawnFlag);
        CustomMobs.logger.debug("RespectWGMobSpawningTypes: " + this.respectWGMobSpawningTypesFlag);
        CustomMobs.logger.debug("RemoveCustomMobsOnChunkUnload: " + this.removeCustomMobsOnChunkUnload);
        CustomMobs.logger.debug("RemoveLimitedMobsOnChunkUnload: " + this.removeLimitedMobsAtChUnload);
        CustomMobs.logger.debug("BannedMobCommands: " + Arrays.toString(this.bannedMobCommands.toArray()));
        CustomMobs.logger.debug("SpawnlimitResetCommand: " + Arrays.toString(this.spawnLimitResetCommands.toArray()));
    }
    
    public boolean debug() {
        return this.debug;
    }
    
    public boolean useFullControl() {
        return this.fullcontrolUsage;
    }
    
    public int spawnThreshold() {
        return this.spawnThreshold;
    }
    
    public boolean spawnRespawnMobsAtStartup() {
        return this.spawnAtStartup;
    }
    
    public boolean removeLimitedMobsOnChunkUnload() {
        return this.removeLimitedMobsAtChUnload;
    }
    
    public boolean removeCustomMobOnChunkUnload() {
        return this.removeCustomMobsOnChunkUnload;
    }
    
    public boolean respectWGMobSpawningFlag() {
        return this.respectWGMobSpawningTypesFlag;
    }
    
    public boolean respectWGDenySpawnFlag() {
        return this.respectWGDenySpawnFlag;
    }
    
    public boolean enablePluginMetrics() {
        return this.enableMetrics;
    }
    
    public int spawnRespawnMobsAtStartupDelay() {
        return this.spawnAtStartupDelay;
    }
    
    public int spawnFrequency() {
        return this.frequency;
    }
    
    public String getLanguageFileConfiguration() {
        return this.languageFile;
    }
    
    public int worldSpawnerTickSpeed() {
        return this.worldSpawnerTickSpeed;
    }
    
    public int spawnerRange() {
        return this.spawnerRange;
    }
    
    public int leashViolationTolerance() {
        return this.leashViolationTolerance;
    }
    
    public List<String> bannedMobCommands() {
        return this.bannedMobCommands;
    }
    
    public List<String> spawnLimitFlushCommands() {
        return this.spawnLimitResetCommands;
    }
}

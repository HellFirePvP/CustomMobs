package de.hellfirepvp.file.write;

import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.block.Biome;
import java.util.ArrayList;
import de.hellfirepvp.api.data.ISpawnSettings;
import org.bukkit.configuration.file.YamlConfiguration;
import de.hellfirepvp.CustomMobs;
import java.io.IOException;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.api.data.callback.SpawnSettingsCallback;

public class SpawnSettingsWriter
{
    public static SpawnSettingsCallback resetSpawnSettings(final String name) {
        final YamlConfiguration config = LibConfiguration.getSpawnSettingsConfiguration();
        if (!config.contains(name)) {
            return SpawnSettingsCallback.MOB_DOESNT_EXIST;
        }
        config.set(name, (Object)null);
        try {
            config.save(LibConfiguration.getSpawnSettingsFile());
        }
        catch (IOException ignored) {
            return SpawnSettingsCallback.IO_EXCEPTION;
        }
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnSettings().resolveSettings();
        CustomMobs.instance.getWorldSpawnExecutor().loadData();
        return SpawnSettingsCallback.SUCCESS;
    }
    
    public static SpawnSettingsCallback setSpawnSettings(final String name, final ISpawnSettings settings) {
        final YamlConfiguration config = LibConfiguration.getSpawnSettingsConfiguration();
        if (config.contains(name)) {
            return SpawnSettingsCallback.MOB_ALREADY_EXISTS;
        }
        final ConfigurationSection section = config.createSection(name);
        final boolean grpSpawn = settings.doesSpawnInGroup();
        final int grpAmount = settings.averageGroupAmount();
        final double spawnRate = settings.getSpawnRate();
        final List<Biome> biomes = settings.areBiomesSpecified() ? settings.getSpecifiedBiomes() : new ArrayList<Biome>();
        final List<String> worlds = settings.areWorldsSpecified() ? settings.getSpecifiedWorlds() : new ArrayList<String>();
        final List<String> regions = settings.areRegionsSpecified() ? settings.getSpecifiedRegions() : new ArrayList<String>();
        final List<String> stringBiomes = new ArrayList<String>();
        biomes.stream().filter(b -> !stringBiomes.contains(b.name())).forEach(b -> stringBiomes.add(b.name()));
        section.set("groupSpawn", (Object)grpSpawn);
        section.set("groupAmount", (Object)grpAmount);
        section.set("spawnRate", (Object)spawnRate);
        section.set("worlds", (Object)worlds);
        section.set("biomes", (Object)stringBiomes);
        section.set("regions", (Object)regions);
        try {
            config.save(LibConfiguration.getSpawnSettingsFile());
        }
        catch (IOException ignored) {
            return SpawnSettingsCallback.IO_EXCEPTION;
        }
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnSettings().resolveSettings();
        CustomMobs.instance.getWorldSpawnExecutor().loadData();
        return SpawnSettingsCallback.SUCCESS;
    }
}

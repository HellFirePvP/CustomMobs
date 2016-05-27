package de.hellfirepvp.file.write;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.SpawnSettingsHolder;
import de.hellfirepvp.data.callback.SpawnSettingsCallback;
import de.hellfirepvp.lib.LibConfiguration;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnSettingsWriter
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:03
 */
public class SpawnSettingsWriter {

    public static SpawnSettingsCallback resetSpawnSettings(String name) {
        YamlConfiguration config = LibConfiguration.getSpawnSettingsConfiguration();

        if(!config.contains(name))
            return SpawnSettingsCallback.MOB_DOESNT_EXIST;

        config.set(name, null);

        try {
            config.save(LibConfiguration.getSpawnSettingsFile());
        } catch (IOException ignored) {
            return SpawnSettingsCallback.IO_EXCEPTION;
        }

        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnSettings().resolveSettings();
        CustomMobs.instance.getRandomWorldSpawner().loadData();

        return SpawnSettingsCallback.SUCCESS;
    }

    public static SpawnSettingsCallback setSpawnSettings(String name, SpawnSettingsHolder.SpawnSettings settings) {
        YamlConfiguration config = LibConfiguration.getSpawnSettingsConfiguration();

        if(config.contains(name))
            return SpawnSettingsCallback.MOB_ALREADY_EXISTS;

        ConfigurationSection section = config.createSection(name);

        boolean grpSpawn = settings.doesSpawnInGroup();
        int grpAmount = settings.averageGroupAmount();
        double spawnRate = settings.getSpawnRate();

        List<Biome> biomes = settings.areBiomesSpecified() ? settings.getSpecifiedBiomes() : new ArrayList<>();
        List<String> worlds = settings.areWorldsSpecified() ? settings.getSpecifiedWorlds() : new ArrayList<>();
        List<String> regions = settings.areRegionsSpecified() ? settings.getSpecifiedRegions() : new ArrayList<>();

        List<String> stringBiomes = new ArrayList<>();
        biomes.stream().filter(b -> !stringBiomes.contains(b.name())).forEach(b -> stringBiomes.add(b.name()));

        section.set(SPAWNSETTINGS_DATA_GROUPSPAWN_BOOL, grpSpawn);
        section.set(SPAWNSETTINGS_DATA_GROUPSPAWN_AMOUNT, grpAmount);
        section.set(SPAWNSETTINGS_DATA_SPAWNRATE, spawnRate);

        section.set(SPAWNSETTINGS_DATA_WORLDS, worlds);
        section.set(SPAWNSETTINGS_DATA_BIOMES, stringBiomes);
        section.set(SPAWNSETTINGS_DATA_REGIONS, regions);

        try {
            config.save(LibConfiguration.getSpawnSettingsFile());
        } catch (IOException ignored) {
            return SpawnSettingsCallback.IO_EXCEPTION;
        }

        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnSettings().resolveSettings();
        CustomMobs.instance.getRandomWorldSpawner().loadData();

        return SpawnSettingsCallback.SUCCESS;
    }

}

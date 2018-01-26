package de.hellfirepvp.file.read;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.SpawnSettingsHolder;
import de.hellfirepvp.lib.LibConfiguration;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnSettingsReader
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:03
 */
public class SpawnSettingsReader {

    public static void readAllSettings(Map<String, SpawnSettingsHolder.SpawnSettings> out) {
        YamlConfiguration config = LibConfiguration.getSpawnSettingsConfiguration();
        Set<String> configuredMobs = config.getKeys(false);
        for(String name : configuredMobs) {
            if(CustomMobs.instance.getMobDataHolder().getCustomMob(name) == null) {
                config.set(name, null);
                continue;
            }

            ConfigurationSection section = config.getConfigurationSection(name);

            boolean groupSpawn = section.getBoolean(SPAWNSETTINGS_DATA_GROUPSPAWN_BOOL, false);
            int groupAmount = section.getInt(SPAWNSETTINGS_DATA_GROUPSPAWN_AMOUNT, 0);
            double spawnRate = section.getDouble(SPAWNSETTINGS_DATA_SPAWNRATE, 1.0);

            List<String> biomeStrings = section.getStringList(SPAWNSETTINGS_DATA_BIOMES);
            List<String> worlds = section.getStringList(SPAWNSETTINGS_DATA_WORLDS);
            List<String> regions = section.getStringList(SPAWNSETTINGS_DATA_REGIONS);

            List<Biome> biomes = new ArrayList<>();
            for(String biomeStr : biomeStrings) {
                Biome b = Biome.valueOf(biomeStr);
                if(b != null) biomes.add(b);
            }

            SpawnSettingsHolder.SpawnSettings settings = new SpawnSettingsHolder.SpawnSettings(groupSpawn, !biomes.isEmpty(), !worlds.isEmpty(), !regions.isEmpty(), groupAmount, biomes, worlds, regions, spawnRate);
            out.put(name, settings);
        }
    }
}

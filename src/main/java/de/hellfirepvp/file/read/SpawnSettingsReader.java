package de.hellfirepvp.file.read;

import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.block.Biome;
import java.util.ArrayList;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.data.SpawnSettingsHolder;
import java.util.Map;

public class SpawnSettingsReader
{
    public static void readAllSettings(final Map<String, SpawnSettingsHolder.SpawnSettings> out) {
        final YamlConfiguration config = LibConfiguration.getSpawnSettingsConfiguration();
        final Set<String> configuredMobs = (Set<String>)config.getKeys(false);
        for (final String name : configuredMobs) {
            if (CustomMobs.instance.getMobDataHolder().getCustomMob(name) == null) {
                config.set(name, (Object)null);
            }
            else {
                final ConfigurationSection section = config.getConfigurationSection(name);
                final boolean groupSpawn = section.getBoolean("groupSpawn", false);
                final int groupAmount = section.getInt("groupAmount", 0);
                final double spawnRate = section.getDouble("spawnRate", 1.0);
                final List<String> biomeStrings = (List<String>)section.getStringList("biomes");
                final List<String> worlds = (List<String>)section.getStringList("worlds");
                final List<String> regions = (List<String>)section.getStringList("regions");
                final List<Biome> biomes = new ArrayList<Biome>();
                for (final String biomeStr : biomeStrings) {
                    final Biome b = Biome.valueOf(biomeStr);
                    if (b != null) {
                        biomes.add(b);
                    }
                }
                final SpawnSettingsHolder.SpawnSettings settings = new SpawnSettingsHolder.SpawnSettings(groupSpawn, !biomes.isEmpty(), !worlds.isEmpty(), !regions.isEmpty(), groupAmount, biomes, worlds, regions, spawnRate);
                out.put(name, settings);
            }
        }
    }
}

package de.hellfirepvp.data;

import de.hellfirepvp.file.read.SpawnSettingsReader;
import org.bukkit.block.Biome;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnSettingsHolder
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:04
 */
public final class SpawnSettingsHolder {

    private Map<String, SpawnSettings> settings = new HashMap<>();

    public void resolveSettings() {
        settings.clear();
        SpawnSettingsReader.readAllSettings(settings);
    }

    public SpawnSettings getSettings(String name) {
        return settings.get(name);
    }

    public Collection<String> getMobNamesWithSettings() {
        return Collections.unmodifiableCollection(settings.keySet());
    }

    public static class SpawnSettings {

        public final boolean groupSpawn, biomeSpecified, worldsSpecified, regionsSpecified;
        public final int groupAmount;
        public final List<Biome> biomes;
        public final List<String> worlds, regions;
        public final double spawnRate;

        public SpawnSettings(boolean groupSpawn, boolean biomeSpecified, boolean worldsSpecified, boolean regionsSpecified, int groupAmount, List<Biome> biomes, List<String> worlds, List<String> regions, double spawnRate) {
            this.groupSpawn = groupSpawn;
            this.biomeSpecified = biomeSpecified;
            this.worldsSpecified = worldsSpecified;
            this.regionsSpecified = regionsSpecified;
            this.groupAmount = groupAmount;
            this.biomes = biomes;
            this.worlds = worlds;
            this.regions = regions;
            this.spawnRate = spawnRate;
        }

        public boolean areBiomesSpecified() {
            return biomeSpecified;
        }

        public boolean areWorldsSpecified() {
            return worldsSpecified;
        }

        public boolean areRegionsSpecified() {
            return regionsSpecified;
        }

        public List<Biome> getSpecifiedBiomes() {
            return Collections.unmodifiableList(biomes);
        }

        public List<String> getSpecifiedWorlds() {
            return Collections.unmodifiableList(worlds);
        }

        public List<String> getSpecifiedRegions() {
            return Collections.unmodifiableList(regions);
        }

        public boolean doesSpawnInGroup() {
            return groupSpawn;
        }

        public int averageGroupAmount() {
            return groupAmount;
        }

        public double getSpawnRate() {
            return spawnRate;
        }
    }

}

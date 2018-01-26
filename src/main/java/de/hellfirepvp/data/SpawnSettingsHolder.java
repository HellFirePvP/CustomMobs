package de.hellfirepvp.data;

import de.hellfirepvp.api.data.ISpawnSettings;
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

    public static class SpawnSettings implements ISpawnSettings {

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

        @Override
        public boolean areBiomesSpecified() {
            return biomeSpecified;
        }

        @Override
        public boolean areWorldsSpecified() {
            return worldsSpecified;
        }

        @Override
        public boolean areRegionsSpecified() {
            return regionsSpecified;
        }

        @Override
        public List<Biome> getSpecifiedBiomes() {
            return Collections.unmodifiableList(biomes);
        }

        @Override
        public List<String> getSpecifiedWorlds() {
            return Collections.unmodifiableList(worlds);
        }

        @Override
        public List<String> getSpecifiedRegions() {
            return Collections.unmodifiableList(regions);
        }

        @Override
        public boolean doesSpawnInGroup() {
            return groupSpawn;
        }

        @Override
        public int averageGroupAmount() {
            return groupAmount;
        }

        @Override
        public double getSpawnRate() {
            return spawnRate;
        }
    }

}

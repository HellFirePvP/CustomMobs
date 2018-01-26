package de.hellfirepvp.data;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.ISpawnSettings;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.api.data.callback.SpawnSettingsCallback;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnSettingsEditor
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:34
 */
public class SpawnSettingsEditor implements ISpawnSettingsEditor {

    @Override
    public ISpawnSettingsEditor.ISpawnSettingsBuilder newSpawnSettingsBuilder() {
        return new SpawnSettingsBuilder();
    }

    @Override
    public SpawnSettingsCallback setSpawnRandomly(ICustomMob mob, ISpawnSettings spawnSettings) {
        return SpawnSettingsWriter.setSpawnSettings(mob.getName(), spawnSettings);
    }

    @Override
    public SpawnSettingsCallback resetSpawnRandomly(ICustomMob mob) {
        return SpawnSettingsWriter.resetSpawnSettings(mob.getName());
    }

    public static class SpawnSettingsBuilder implements ISpawnSettingsBuilder {

        private boolean spawnInGroups = false;
        private double spawnRate = 0.1;
        private int averageGroupAmount = 1;
        private boolean biomesSpecified = false;
        private boolean worldsSpecified = false;
        private boolean regionsSpecified = false;

        private List<Biome> biomes = new ArrayList<>();
        private List<String> worlds = new ArrayList<>();
        private List<String> regions = new ArrayList<>();

        @Override
        public ISpawnSettingsEditor.ISpawnSettingsBuilder setSpawnInGroups(boolean spawnInGroups) {
            this.spawnInGroups = spawnInGroups;
            return this;
        }

        @Override
        public ISpawnSettingsEditor.ISpawnSettingsBuilder setSpawnRate(double spawnRate) {
            this.spawnRate = spawnRate;
            return this;
        }

        @Override
        public ISpawnSettingsEditor.ISpawnSettingsBuilder setAverageGroupAmount(int averageGroupAmount) {
            this.averageGroupAmount = averageGroupAmount;
            return this;
        }

        @Override
        public ISpawnSettingsEditor.ISpawnSettingsBuilderForcedBiome specifyBiomes() {
            this.biomesSpecified = true;
            return new ForcedBiomes(this);
        }

        @Override
        public ISpawnSettingsEditor.ISpawnSettingsBuilderForcedWorld specifyWorlds() {
            this.worldsSpecified = true;
            return new ForcedWorld(this);
        }

        @Override
        public ISpawnSettingsEditor.ISpawnSettingsBuilderForcedRegion specifyRegions() {
            this.regionsSpecified = true;
            return new ForcedRegion(this);
        }

        @Override
        public ISpawnSettings build() {
            return new SpawnSettingsHolder.SpawnSettings(spawnInGroups, biomesSpecified, worldsSpecified, regionsSpecified,
                    averageGroupAmount, biomes, worlds, regions, spawnRate);
        }

        public static class ForcedBiomes implements ISpawnSettingsEditor.ISpawnSettingsBuilderForcedBiome {

            private final SpawnSettingsBuilder builder;

            private ForcedBiomes(SpawnSettingsBuilder builder) {
                this.builder = builder;
            }

            @Override
            public ISpawnSettingsEditor.ISpawnSettingsBuilder setBiomes(List<Biome> biomes) {
                builder.biomes = biomes;
                return builder;
            }

        }

        public static class ForcedWorld implements ISpawnSettingsEditor.ISpawnSettingsBuilderForcedWorld {

            private final SpawnSettingsBuilder builder;

            private ForcedWorld(SpawnSettingsBuilder builder) {
                this.builder = builder;
            }

            @Override
            public ISpawnSettingsEditor.ISpawnSettingsBuilder setWorlds(List<String> worlds) {
                builder.worlds = worlds;
                return builder;
            }

        }

        public static class ForcedRegion implements ISpawnSettingsEditor.ISpawnSettingsBuilderForcedRegion {

            private final SpawnSettingsBuilder builder;

            private ForcedRegion(SpawnSettingsBuilder builder) {
                this.builder = builder;
            }

            @Override
            public ISpawnSettingsEditor.ISpawnSettingsBuilder setRegions(List<String> regions) {
                builder.regions = regions;
                return builder;
            }

        }

    }

}

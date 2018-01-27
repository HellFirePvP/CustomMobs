package de.hellfirepvp.data;

import java.util.ArrayList;
import org.bukkit.block.Biome;
import java.util.List;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import de.hellfirepvp.api.data.callback.SpawnSettingsCallback;
import de.hellfirepvp.api.data.ISpawnSettings;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;

public class SpawnSettingsEditor implements ISpawnSettingsEditor
{
    @Override
    public ISpawnSettingsBuilder newSpawnSettingsBuilder() {
        return new SpawnSettingsBuilder();
    }
    
    @Override
    public SpawnSettingsCallback setSpawnRandomly(final ICustomMob mob, final ISpawnSettings spawnSettings) {
        return SpawnSettingsWriter.setSpawnSettings(mob.getName(), spawnSettings);
    }
    
    @Override
    public SpawnSettingsCallback resetSpawnRandomly(final ICustomMob mob) {
        return SpawnSettingsWriter.resetSpawnSettings(mob.getName());
    }
    
    public static class SpawnSettingsBuilder implements ISpawnSettingsBuilder
    {
        private boolean spawnInGroups;
        private double spawnRate;
        private int averageGroupAmount;
        private boolean biomesSpecified;
        private boolean worldsSpecified;
        private boolean regionsSpecified;
        private List<Biome> biomes;
        private List<String> worlds;
        private List<String> regions;
        
        public SpawnSettingsBuilder() {
            this.spawnInGroups = false;
            this.spawnRate = 0.1;
            this.averageGroupAmount = 1;
            this.biomesSpecified = false;
            this.worldsSpecified = false;
            this.regionsSpecified = false;
            this.biomes = new ArrayList<Biome>();
            this.worlds = new ArrayList<String>();
            this.regions = new ArrayList<String>();
        }
        
        @Override
        public ISpawnSettingsBuilder setSpawnInGroups(final boolean spawnInGroups) {
            this.spawnInGroups = spawnInGroups;
            return this;
        }
        
        @Override
        public ISpawnSettingsBuilder setSpawnRate(final double spawnRate) {
            this.spawnRate = spawnRate;
            return this;
        }
        
        @Override
        public ISpawnSettingsBuilder setAverageGroupAmount(final int averageGroupAmount) {
            this.averageGroupAmount = averageGroupAmount;
            return this;
        }
        
        @Override
        public ISpawnSettingsBuilderForcedBiome specifyBiomes() {
            this.biomesSpecified = true;
            return new ForcedBiomes(this);
        }
        
        @Override
        public ISpawnSettingsBuilderForcedWorld specifyWorlds() {
            this.worldsSpecified = true;
            return new ForcedWorld(this);
        }
        
        @Override
        public ISpawnSettingsBuilderForcedRegion specifyRegions() {
            this.regionsSpecified = true;
            return new ForcedRegion(this);
        }
        
        @Override
        public ISpawnSettings build() {
            return new SpawnSettingsHolder.SpawnSettings(this.spawnInGroups, this.biomesSpecified, this.worldsSpecified, this.regionsSpecified, this.averageGroupAmount, this.biomes, this.worlds, this.regions, this.spawnRate);
        }
        
        public static class ForcedBiomes implements ISpawnSettingsBuilderForcedBiome
        {
            private final SpawnSettingsBuilder builder;
            
            private ForcedBiomes(final SpawnSettingsBuilder builder) {
                this.builder = builder;
            }
            
            @Override
            public ISpawnSettingsBuilder setBiomes(final List<Biome> biomes) {
                this.builder.biomes = biomes;
                return this.builder;
            }
        }
        
        public static class ForcedWorld implements ISpawnSettingsBuilderForcedWorld
        {
            private final SpawnSettingsBuilder builder;
            
            private ForcedWorld(final SpawnSettingsBuilder builder) {
                this.builder = builder;
            }
            
            @Override
            public ISpawnSettingsBuilder setWorlds(final List<String> worlds) {
                this.builder.worlds = worlds;
                return this.builder;
            }
        }
        
        public static class ForcedRegion implements ISpawnSettingsBuilderForcedRegion
        {
            private final SpawnSettingsBuilder builder;
            
            private ForcedRegion(final SpawnSettingsBuilder builder) {
                this.builder = builder;
            }
            
            @Override
            public ISpawnSettingsBuilder setRegions(final List<String> regions) {
                this.builder.regions = regions;
                return this.builder;
            }
        }
    }
}

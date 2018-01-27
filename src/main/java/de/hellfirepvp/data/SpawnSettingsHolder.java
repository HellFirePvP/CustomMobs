package de.hellfirepvp.data;

import org.bukkit.block.Biome;
import java.util.List;
import de.hellfirepvp.api.data.ISpawnSettings;
import java.util.Collections;
import java.util.Collection;
import de.hellfirepvp.file.read.SpawnSettingsReader;
import java.util.HashMap;
import java.util.Map;

public final class SpawnSettingsHolder
{
    private Map<String, SpawnSettings> settings;
    
    public SpawnSettingsHolder() {
        this.settings = new HashMap<String, SpawnSettings>();
    }
    
    public void resolveSettings() {
        this.settings.clear();
        SpawnSettingsReader.readAllSettings(this.settings);
    }
    
    public SpawnSettings getSettings(final String name) {
        return this.settings.get(name);
    }
    
    public Collection<String> getMobNamesWithSettings() {
        return Collections.unmodifiableCollection((Collection<? extends String>)this.settings.keySet());
    }
    
    public static class SpawnSettings implements ISpawnSettings
    {
        public final boolean groupSpawn;
        public final boolean biomeSpecified;
        public final boolean worldsSpecified;
        public final boolean regionsSpecified;
        public final int groupAmount;
        public final List<Biome> biomes;
        public final List<String> worlds;
        public final List<String> regions;
        public final double spawnRate;
        
        public SpawnSettings(final boolean groupSpawn, final boolean biomeSpecified, final boolean worldsSpecified, final boolean regionsSpecified, final int groupAmount, final List<Biome> biomes, final List<String> worlds, final List<String> regions, final double spawnRate) {
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
            return this.biomeSpecified;
        }
        
        @Override
        public boolean areWorldsSpecified() {
            return this.worldsSpecified;
        }
        
        @Override
        public boolean areRegionsSpecified() {
            return this.regionsSpecified;
        }
        
        @Override
        public List<Biome> getSpecifiedBiomes() {
            return Collections.unmodifiableList((List<? extends Biome>)this.biomes);
        }
        
        @Override
        public List<String> getSpecifiedWorlds() {
            return Collections.unmodifiableList((List<? extends String>)this.worlds);
        }
        
        @Override
        public List<String> getSpecifiedRegions() {
            return Collections.unmodifiableList((List<? extends String>)this.regions);
        }
        
        @Override
        public boolean doesSpawnInGroup() {
            return this.groupSpawn;
        }
        
        @Override
        public int averageGroupAmount() {
            return this.groupAmount;
        }
        
        @Override
        public double getSpawnRate() {
            return this.spawnRate;
        }
    }
}

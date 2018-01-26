package de.hellfirepvp.api.data;

import org.bukkit.block.Biome;
import java.util.List;
import de.hellfirepvp.api.data.callback.SpawnSettingsCallback;

public interface ISpawnSettingsEditor
{
    ISpawnSettingsBuilder newSpawnSettingsBuilder();
    
    SpawnSettingsCallback setSpawnRandomly(final ICustomMob p0, final ISpawnSettings p1);
    
    SpawnSettingsCallback resetSpawnRandomly(final ICustomMob p0);
    
    public interface ISpawnSettingsBuilderForcedRegion
    {
        ISpawnSettingsBuilder setRegions(final List<String> p0);
    }
    
    public interface ISpawnSettingsBuilder
    {
        ISpawnSettingsBuilder setSpawnInGroups(final boolean p0);
        
        ISpawnSettingsBuilder setSpawnRate(final double p0);
        
        ISpawnSettingsBuilder setAverageGroupAmount(final int p0);
        
        ISpawnSettingsBuilderForcedBiome specifyBiomes();
        
        ISpawnSettingsBuilderForcedWorld specifyWorlds();
        
        ISpawnSettingsBuilderForcedRegion specifyRegions();
        
        ISpawnSettings build();
    }
    
    public interface ISpawnSettingsBuilderForcedBiome
    {
        ISpawnSettingsBuilder setBiomes(final List<Biome> p0);
    }
    
    public interface ISpawnSettingsBuilderForcedWorld
    {
        ISpawnSettingsBuilder setWorlds(final List<String> p0);
    }
}

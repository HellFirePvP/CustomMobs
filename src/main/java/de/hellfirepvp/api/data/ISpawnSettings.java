package de.hellfirepvp.api.data;

import org.bukkit.block.Biome;
import java.util.List;

public interface ISpawnSettings
{
    boolean areBiomesSpecified();
    
    boolean areWorldsSpecified();
    
    boolean areRegionsSpecified();
    
    List<Biome> getSpecifiedBiomes();
    
    List<String> getSpecifiedWorlds();
    
    List<String> getSpecifiedRegions();
    
    boolean doesSpawnInGroup();
    
    int averageGroupAmount();
    
    double getSpawnRate();
}

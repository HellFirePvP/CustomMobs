package de.hellfirepvp.api.data;

import org.bukkit.block.Biome;

import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ISpawnSettings
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:31
 */
public interface ISpawnSettings {

    public boolean areBiomesSpecified();

    public boolean areWorldsSpecified();

    public boolean areRegionsSpecified();

    public List<Biome> getSpecifiedBiomes();

    public List<String> getSpecifiedWorlds();

    public List<String> getSpecifiedRegions();

    public boolean doesSpawnInGroup();

    public int averageGroupAmount();

    public double getSpawnRate();

}

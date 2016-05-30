package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.callback.SpawnSettingsCallback;
import org.bukkit.block.Biome;

import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ISpawnSettingsEditor
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:29
 */
public interface ISpawnSettingsEditor {

    public ISpawnSettingsBuilder newSpawnSettingsBuilder();

    public SpawnSettingsCallback setSpawnRandomly(ICustomMob mob, ISpawnSettings spawnSettings);

    public SpawnSettingsCallback resetSpawnRandomly(ICustomMob mob);

    public static interface ISpawnSettingsBuilder {

        public ISpawnSettingsBuilder setSpawnInGroups(boolean spawnInGroups);

        public ISpawnSettingsBuilder setSpawnRate(double spawnRate);

        public ISpawnSettingsBuilder setAverageGroupAmount(int averageGroupAmount);

        public ISpawnSettingsBuilderForcedBiome specifyBiomes();

        public ISpawnSettingsBuilderForcedWorld specifyWorlds();

        public ISpawnSettingsBuilderForcedRegion specifyRegions();

        public ISpawnSettings build();

    }

    public static interface ISpawnSettingsBuilderForcedBiome {

        public ISpawnSettingsBuilder setBiomes(List<Biome> biomes);

    }

    public static interface ISpawnSettingsBuilderForcedWorld {

        public ISpawnSettingsBuilder setWorlds(List<String> worlds);

    }

    public static interface ISpawnSettingsBuilderForcedRegion {

        public ISpawnSettingsBuilder setRegions(List<String> regions);

    }

}

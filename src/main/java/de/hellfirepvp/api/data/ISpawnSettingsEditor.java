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

    /**
     * @return a builder for new spawnsettings
     */
    public ISpawnSettingsBuilder newSpawnSettingsBuilder();

    /**
     * Apply previously built spawnsettings to a custommob.
     *
     * @param mob the mob to apply the spawnsettings to.
     * @param spawnSettings the spawnsettings to be set.
     * @return a callback, containing information about the result.
     */
    public SpawnSettingsCallback setSpawnRandomly(ICustomMob mob, ISpawnSettings spawnSettings);

    /**
     * Resets/removes spawnsettings from a custommob.
     *
     * @param mob the mob to remove the spawnsettings from
     * @return a callback, containing information about the result.
     */
    public SpawnSettingsCallback resetSpawnRandomly(ICustomMob mob);

    public static interface ISpawnSettingsBuilder {

        /**
         * Sets the flag if the mob should spawn in groups.
         *
         * @param spawnInGroups to set.
         * @return the builder instance for chaining.
         */
        public ISpawnSettingsBuilder setSpawnInGroups(boolean spawnInGroups);

        /**
         * Sets the spawnrate in comparison to other custommobs that have been set to spawn randomly
         *
         * @param spawnRate to set.
         * @return the builder instance for chaining.
         */
        public ISpawnSettingsBuilder setSpawnRate(double spawnRate);

        /**
         * In case the mob should spawn in group, this defines the group amount they can max. spawn in.
         *
         * @param averageGroupAmount the max. group amount
         * @return the builder instance for chaining.
         */
        public ISpawnSettingsBuilder setAverageGroupAmount(int averageGroupAmount);

        /**
         * Allows and forces biome specification in the next building-step.
         *
         * @return a forced builder, forcing specification of biomes.
         */
        public ISpawnSettingsBuilderForcedBiome specifyBiomes();

        /**
         * Allows and forces world specification in the next building-step.
         *
         * @return a forced builder, forcing specification of worlds.
         */
        public ISpawnSettingsBuilderForcedWorld specifyWorlds();

        /**
         * Allows and forces region specification in the next building-step.
         * Regions specified are WorldGuard region-names.
         *
         * @return a forced builder, forcing specification of regions.
         */
        public ISpawnSettingsBuilderForcedRegion specifyRegions();

        /**
         * Finish building spawnsettings and return the finished spawnsettings.
         *
         * @return the spawnsettings with the settings defined in the builder.
         */
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

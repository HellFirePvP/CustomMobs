package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.callback.SpawnerDataCallback;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ISpawnerEditor
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:37
 */
public interface ISpawnerEditor {

    /**
     * Defines a location as a custommob spawner.
     * The delay for spawning mobs is randomized.
     *
     * @param mob the mob that should be spawned by the spawner
     * @param location the location that should be a spawner.
     * @return a callback, containing information about the result.
     */
    public SpawnerDataCallback setSpawner(ICustomMob mob, Location location);

    /**
     * Defines a location as a custommob spawner.
     * the delay for spawning will always be 'delay' seconds.
     *
     * @param delay the mobspawner delay in seconds.
     * @param mob the mob that should be spawned by the spawner
     * @param location the location that should be a spawner.
     * @return a callback, containing information about the result.
     */
    public SpawnerDataCallback setSpawner(ICustomMob mob, Location location, Integer delay);

    /**
     * Clears the mobspawner at a specific location
     *
     * @param location the location where a spawner should be removed.
     * @return a callback, containing information about the result.
     */
    public SpawnerDataCallback resetSpawner(Location location);

    /**
     * Try to get a mobspawner at a specific location
     *
     * @param location the location where you guess there is a spawner
     * @return the spawner info of the spawner at the specified location or null if
     *          there is no spawner at the specified location
     */
    @Nullable
    public SpawnerInfo getSpawner(Location location);

    /**
     * Get all spawners that exist on this server.
     *
     * @return a collection of spawner objects, containing all relevant data about a spawner.
     */
    public Collection<SpawnerInfo> getAllSpawners();

    public static class SpawnerInfo {

        private final Location loc;
        private final ISpawner spawner;

        public SpawnerInfo(Location loc, ISpawner spawner) {
            this.loc = loc;
            this.spawner = spawner;
        }

        public Location getLocation() {
            return loc;
        }

        public ISpawner getSpawner() {
            return spawner;
        }

    }

    public static interface ISpawner {

        public ICustomMob getSpawningCustomMob();

        public boolean hasFixedDelay();

        public int getFixedDelay();

    }

}

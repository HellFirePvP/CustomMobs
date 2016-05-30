package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.callback.SpawnerDataCallback;
import org.bukkit.Location;

import java.util.Collection;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ISpawnerEditor
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:37
 */
public interface ISpawnerEditor {

    public SpawnerDataCallback setSpawner(ICustomMob mob, Location location);

    public SpawnerDataCallback setSpawner(ICustomMob mob, Location location, Integer delay);

    public SpawnerDataCallback resetSpawner(Location location);

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

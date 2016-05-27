package de.hellfirepvp.data;

import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.file.read.SpawnerDataReader;
import org.bukkit.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnerDataHolder
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:04
 */
public final class SpawnerDataHolder {

    private Map<Location, Spawner> spawnerData = new HashMap<>();

    public void loadData() {
        spawnerData.clear();

        SpawnerDataReader.readSpawnerData(spawnerData);
    }

    public Spawner getSpawnerAt(Location location) {
        return spawnerData.get(location);
    }

    public Map<Location, Spawner> getSpawners() {
        return Collections.unmodifiableMap(spawnerData);
    }

    public static class Spawner {

        public final CustomMob linked;
        public final int fixedDelay;
        public final boolean hasFixedDelay;

        public Spawner(CustomMob linked, int fixedDelay, boolean hasFixedDelay) {
            this.linked = linked;
            this.fixedDelay = fixedDelay;
            this.hasFixedDelay = hasFixedDelay;
        }
    }

}

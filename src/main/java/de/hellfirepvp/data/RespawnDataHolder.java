package de.hellfirepvp.data;

import de.hellfirepvp.file.read.RespawnDataReader;
import org.bukkit.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * HellFirePvP@Admin
 * Date: 08.05.2015 / 22:12
 * on Project CustomMobs
 * RespawnDataHolder
 */
public final class RespawnDataHolder {

    private Map<String, RespawnSettings> respawnData = new HashMap<>();

    public void loadData() {
        respawnData.clear();

        RespawnDataReader.loadData(respawnData);
    }

    public RespawnSettings getSettings(String mobName) {
        return respawnData.get(mobName);
    }

    public Map<String, RespawnSettings> getRespawnData() {
        return Collections.unmodifiableMap(respawnData);
    }

    public static class RespawnSettings {

        public final Location location;
        public final long respawnTime;

        public RespawnSettings(Location location, long respawnTime) {
            this.location = location;
            this.respawnTime = respawnTime;
        }
    }
}

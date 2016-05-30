package de.hellfirepvp.data;

import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.file.read.RespawnDataReader;
import org.bukkit.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: RespawnDataHolder
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:04
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

    public static class RespawnSettings implements IRespawnEditor.IRespawnSettings {

        public final Location location;
        public final long respawnTime;

        public RespawnSettings(Location location, long respawnTime) {
            this.location = location;
            this.respawnTime = respawnTime;
        }

        @Override
        public Location getLocation() {
            return location;
        }

        @Override
        public long getRespawnDelay() {
            return respawnTime;
        }
    }
}

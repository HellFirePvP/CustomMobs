package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.callback.RespawnDataCallback;
import de.hellfirepvp.data.RespawnEditor;
import org.bukkit.Location;

import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: IRespawnEditor
 * Created by HellFirePvP
 * Date: 30.05.2016 / 10:17
 */
public interface IRespawnEditor {

    public RespawnDataCallback setRespawn(ICustomMob mob, Location location, long delay);

    public RespawnDataCallback resetRespawn(ICustomMob mob);

    public Map<ICustomMob, RespawnEditor.IRespawnSettings> getRespawnSettings();

    public static interface IRespawnSettings {

        public Location getLocation();

        public long getRespawnDelay();

    }

}

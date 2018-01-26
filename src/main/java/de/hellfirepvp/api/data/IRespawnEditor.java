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

    /**
     * Equivalent to the /crespawn add command.
     * Set a custommob to respawn at a specific location after being killed with specific delay.
     *
     * @param mob the mob that should respawn
     * @param location the location the mob should respawn at
     * @param delay the delay of the respawn
     * @return a callback, containing information about the result
     */
    public RespawnDataCallback setRespawn(ICustomMob mob, Location location, long delay);

    /**
     * Equivalent to the /crespawn remove command.
     * Removes a custommob so it doesn't respawn anymore at its defined location.
     *
     * @param mob the mob that should no longer respawn
     * @return a callback, containing information about the result
     */
    public RespawnDataCallback resetRespawn(ICustomMob mob);

    /**
     * Get all custommobs that are specified to respawn
     *
     * @return a map, listing all mobs that are set to respawn and information about their respawn settings.
     */
    public Map<ICustomMob, RespawnEditor.IRespawnSettings> getRespawnSettings();

    public static interface IRespawnSettings {

        public Location getLocation();

        public long getRespawnDelay();

    }

}

package de.hellfirepvp.api.data.callback;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: RespawnDataCallback
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:04
 */
public enum RespawnDataCallback {

    /**
     * The mob doesn't exist
     */
    MOB_DOESNT_EXIST,

    /**
     * The mob is already specified to respawn
     */
    MOB_ALREADY_EXISTS,

    /**
     * Couldn't save the respawn data file
     */
    IO_EXCEPTION,

    /**
     * Success.
     */
    SUCCESS

}

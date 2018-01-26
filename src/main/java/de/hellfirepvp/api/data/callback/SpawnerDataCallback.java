package de.hellfirepvp.api.data.callback;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnerDataCallback
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:04
 */
public enum SpawnerDataCallback {

    /**
     * The location is already specified as a spawner
     */
    LOCATION_OCCUPIED,

    /**
     * The location is not specified as spawner
     */
    LOCATION_NO_SPAWNER,

    /**
     * Can't save the spawner data file
     */
    IO_EXCEPTION,

    /**
     * Success.
     */
    SUCCESS

}

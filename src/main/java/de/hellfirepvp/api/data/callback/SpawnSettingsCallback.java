package de.hellfirepvp.api.data.callback;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnSettingsCallback
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:03
 */
public enum SpawnSettingsCallback {

    /**
     * The mob you want to set to spawn randomly doesn't exist.
     */
    MOB_DOESNT_EXIST,

    /**
     * The mob is already defined to spawn randomly.
     */
    MOB_ALREADY_EXISTS,

    /**
     * Failed to save the spawn settings file.
     */
    IO_EXCEPTION,

    /**
     * Success.
     */
    SUCCESS

}

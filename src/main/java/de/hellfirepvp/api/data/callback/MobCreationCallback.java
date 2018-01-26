package de.hellfirepvp.api.data.callback;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: MobCreationCallback
 * Created by HellFirePvP
 * Date: 30.05.2016 / 10:44
 */
public enum MobCreationCallback {

    /**
     * The name of the mob is already taken
     */
    NAME_TAKEN,

    /**
     * The specified mobtype is unknown
     */
    UNKNOWN_TYPE,

    /**
     * It failed for some reason
     * happens if the passed entity is null or the mobtype is unknown, but got passed onwards to deeper nms integrations
     */
    FAILED,

    /**
     * Success.
     */
    SUCCESS

}

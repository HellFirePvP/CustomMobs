package de.hellfirepvp.util;

import org.spigotmc.SpigotConfig;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ServerType
 * Created by HellFirePvP
 * Date: 12.09.2016 / 21:43
 */
public enum ServerType {

    BUKKIT, SPIGOT;

    private static boolean resolved = false;
    private static ServerType assumedType;

    public static void resolve() {
        try {
            double health = SpigotConfig.maxHealth;
            health += 1;
            assumedType = SPIGOT;
        } catch (Throwable tr) {
            assumedType = BUKKIT;
        }
        resolved = true;
    }

    public static ServerType getResolvedType() {
        if(!resolved) {
            throw new IllegalStateException("Didn't resolve ServerType yet!");
        }

        return assumedType;
    }

}

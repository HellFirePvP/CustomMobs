package de.hellfirepvp.util;

import org.spigotmc.SpigotConfig;

public enum ServerType
{
    BUKKIT, 
    SPIGOT;
    
    private static boolean resolved;
    private static ServerType assumedType;
    
    public static void resolve() {
        try {
            double health = SpigotConfig.maxHealth;
            ++health;
            ServerType.assumedType = ServerType.SPIGOT;
        }
        catch (Throwable tr) {
            ServerType.assumedType = ServerType.BUKKIT;
        }
        ServerType.resolved = true;
    }
    
    public static ServerType getResolvedType() {
        if (!ServerType.resolved) {
            throw new IllegalStateException("Didn't resolve ServerType yet!");
        }
        return ServerType.assumedType;
    }
    
    static {
        ServerType.resolved = false;
    }
}

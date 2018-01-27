package de.hellfirepvp.util;

import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.Random;

public class LocationUtils
{
    public static final Random RANDOM;
    
    public static String serializeExactLoc(final Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();
    }
    
    public static String serializeBlockLoc(final Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }
    
    public static Location deserializeExactLoc(final String serialized) {
        try {
            final String[] parts = serialized.split(";");
            return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        }
        catch (Exception ignored) {
            return null;
        }
    }
    
    public static Location deserializeBlockLoc(final String serialized) {
        try {
            final String[] parts = serialized.split(";");
            return new Location(Bukkit.getWorld(parts[0]), (double)Integer.parseInt(parts[1]), (double)Integer.parseInt(parts[2]), (double)Integer.parseInt(parts[3]));
        }
        catch (Exception ignored) {
            return null;
        }
    }
    
    public static Location toRand(final Location l, final int range) {
        final Location nw = l.clone().add((double)LocationUtils.RANDOM.nextInt(range), (double)LocationUtils.RANDOM.nextInt(range), (double)LocationUtils.RANDOM.nextInt(range));
        Location newL = iterateDown(nw);
        if (newL.equals((Object)nw)) {
            newL = iterateUp(nw);
        }
        return newL;
    }
    
    private static Location iterateUp(Location nw) {
        Location last = nw;
        for (int x = 0; x < 15; ++x) {
            nw = nw.add(0.0, 1.0, 0.0);
            if (nw.getBlock().getType().equals((Object)Material.AIR)) {
                return nw;
            }
            last = nw;
        }
        return last;
    }
    
    private static Location iterateDown(Location nw) {
        Location last = nw;
        for (int x = 0; x < 15; ++x) {
            nw = nw.subtract(0.0, 1.0, 0.0);
            if (!nw.getBlock().getType().equals((Object)Material.AIR)) {
                return last.add(0.0, 1.0, 0.0);
            }
            last = nw;
        }
        return last;
    }
    
    static {
        RANDOM = new Random();
    }
}

package de.hellfirepvp.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Random;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: LocationUtils
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:00
 */
public class LocationUtils {

    public static final Random RANDOM = new Random();

    public static String serializeExactLoc(Location location) {
        return location.getWorld().getName() + ";" + location.getX()
                + ";" + location.getY() + ";" + location.getZ();
    }

    public static String serializeBlockLoc(Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX()
                + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

    public static Location deserializeExactLoc(String serialized) {
        try {
            String[] parts = serialized.split(";");
            return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]),
                    Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Location deserializeBlockLoc(String serialized) {
        try {
            String[] parts = serialized.split(";");
            return new Location(Bukkit.getWorld(parts[0]), Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Location toRand(Location l, int range) {
        Location nw = l.clone().add(RANDOM.nextInt(range), RANDOM.nextInt(range), RANDOM.nextInt(range));
        Location newL = iterateDown(nw);
        if (newL.equals(nw)) newL = iterateUp(nw);
        return newL;
    }

    private static Location iterateUp(Location nw) {
        Location last = nw;
        for (int x = 0; x < 15; x++) {
            nw = nw.add(0.0D, 1.0D, 0.0D);
            if (!nw.getBlock().getType().equals(Material.AIR)) {
                last = nw;
            }
            else {
                return nw;
            }
        }
        return last;
    }

    private static Location iterateDown(Location nw) {
        Location last = nw;
        for (int x = 0; x < 15; x++) {
            nw = nw.subtract(0.0D, 1.0D, 0.0D);
            if (nw.getBlock().getType().equals(Material.AIR)) {
                last = nw;
            }
            else {
                return last.add(0.0D, 1.0D, 0.0D);
            }
        }
        return last;
    }

}

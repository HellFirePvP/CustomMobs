package de.hellfirepvp.util;

import org.bukkit.WorldBorder;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WorldBorderUtil
 * Created by HellFirePvP
 * Date: 26.05.2016 / 18:42
 */
public class WorldBorderUtil {

    private WorldBorder worldBorder;

    public WorldBorderUtil(WorldBorder worldBorder) {
        this.worldBorder = worldBorder;
    }

    public boolean isInside(int chX, int chZ) {
        return ((chX << 4) + 15 > minX()) && (chX << 4 < maxX()) && ((chZ << 4) + 15 > minZ()) && (chX << 4 < maxZ());
    }

    public double getCenterX() {
        return worldBorder.getCenter().getX();
    }

    public double getCenterZ() {
        return worldBorder.getCenter().getZ();
    }

    public double getSize() {
        return worldBorder.getSize();
    }

    public double minX() {
        double d0 = getCenterX() - getSize() / 2.0D;
        if (d0 < -29999984) {
            d0 = -29999984;
        }
        return d0;
    }

    public double minZ() {
        double d0 = getCenterZ() - getSize() / 2.0D;
        if (d0 < -29999984) {
            d0 = -29999984;
        }
        return d0;
    }

    public double maxX() {
        double d0 = getCenterX() + getSize() / 2.0D;
        if (d0 > 29999984) {
            d0 = 29999984;
        }
        return d0;
    }

    public double maxZ() {
        double d0 = getCenterZ() + getSize() / 2.0D;
        if (d0 > 29999984) {
            d0 = 29999984;
        }
        return d0;
    }

}

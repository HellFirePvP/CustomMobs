package de.hellfirepvp.util;

import org.bukkit.WorldBorder;

public class WorldBorderUtil
{
    private WorldBorder worldBorder;
    
    public WorldBorderUtil(final WorldBorder worldBorder) {
        this.worldBorder = worldBorder;
    }
    
    public boolean isInside(final int chX, final int chZ) {
        return (chX << 4) + 15 > this.minX() && chX << 4 < this.maxX() && (chZ << 4) + 15 > this.minZ() && chX << 4 < this.maxZ();
    }
    
    public double getCenterX() {
        return this.worldBorder.getCenter().getX();
    }
    
    public double getCenterZ() {
        return this.worldBorder.getCenter().getZ();
    }
    
    public double getSize() {
        return this.worldBorder.getSize();
    }
    
    public double minX() {
        double d0 = this.getCenterX() - this.getSize() / 2.0;
        if (d0 < -2.9999984E7) {
            d0 = -2.9999984E7;
        }
        return d0;
    }
    
    public double minZ() {
        double d0 = this.getCenterZ() - this.getSize() / 2.0;
        if (d0 < -2.9999984E7) {
            d0 = -2.9999984E7;
        }
        return d0;
    }
    
    public double maxX() {
        double d0 = this.getCenterX() + this.getSize() / 2.0;
        if (d0 > 2.9999984E7) {
            d0 = 2.9999984E7;
        }
        return d0;
    }
    
    public double maxZ() {
        double d0 = this.getCenterZ() + this.getSize() / 2.0;
        if (d0 > 2.9999984E7) {
            d0 = 2.9999984E7;
        }
        return d0;
    }
}

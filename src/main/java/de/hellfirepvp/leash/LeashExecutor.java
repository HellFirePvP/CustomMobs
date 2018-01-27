package de.hellfirepvp.leash;

import java.util.HashMap;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.entity.Entity;
import org.bukkit.World;
import java.util.UUID;
import org.bukkit.plugin.Plugin;
import de.hellfirepvp.CustomMobs;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import java.util.Map;

public class LeashExecutor
{
    private static Map<LivingEntity, Integer> leashed;
    
    public static void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)CustomMobs.instance, LeashExecutor::doTick, 5L, 5L);
    }
    
    public static void doTick() {
        try {
            synchronized (LeashManager.leashMap) {
            Label_0020:
                while (true) {
                    for (final UUID uuid : LeashManager.leashMap.keySet()) {
                        if (isLeashActive(uuid)) {
                            continue;
                        }
                        for (final World world : Bukkit.getWorlds()) {
                            for (final Entity e : world.getEntities()) {
                                if (e != null && e instanceof LivingEntity && e.getUniqueId().equals(uuid)) {
                                    LeashExecutor.leashed.put((LivingEntity)e, 0);
                                    continue Label_0020;
                                }
                            }
                        }
                    }
                    break;
                }
            }
            final int leashTolerance = CustomMobs.instance.getConfigHandler().leashViolationTolerance();
            final Iterator<LivingEntity> iterator = LeashExecutor.leashed.keySet().iterator();
            while (iterator.hasNext()) {
                final LivingEntity le = iterator.next();
                final LeashManager.ActiveLeashInfo info = LeashManager.leashMap.get(le.getUniqueId());
                if (info == null) {
                    iterator.remove();
                }
                else {
                    final World eW = le.getWorld();
                    final Location leashCenter = info.to;
                    if (!eW.equals(leashCenter.getWorld())) {
                        le.teleport(leashCenter);
                    }
                    else {
                        final double dst = le.getLocation().distance(info.to);
                        if (dst > info.maxRange) {
                            final int violationCounter = LeashExecutor.leashed.get(le);
                            if (violationCounter >= leashTolerance) {
                                LeashExecutor.leashed.put(le, 0);
                                le.teleport(info.to);
                            }
                            else {
                                final float perc = violationCounter / leashTolerance;
                                final Vector pushVec = info.to.toVector().clone().subtract(le.getLocation().toVector().clone()).multiply(perc);
                                le.setVelocity(pushVec);
                                LeashExecutor.leashed.put(le, LeashExecutor.leashed.get(le) + 1);
                            }
                        }
                        else {
                            LeashExecutor.leashed.put(le, 0);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void cutLeash(final LivingEntity le) {
        LeashExecutor.leashed.remove(le);
    }
    
    public static void leash(final LivingEntity le) {
        LeashExecutor.leashed.put(le, 0);
    }
    
    private static boolean isLeashActive(final UUID uuid) {
        for (final LivingEntity le : LeashExecutor.leashed.keySet()) {
            if (le.getUniqueId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        LeashExecutor.leashed = new HashMap<LivingEntity, Integer>();
    }
}

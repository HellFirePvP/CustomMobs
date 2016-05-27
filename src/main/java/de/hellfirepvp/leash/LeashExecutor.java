package de.hellfirepvp.leash;

import de.hellfirepvp.CustomMobs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: LeashExecutor
 * Created by HellFirePvP
 * Date: 26.05.2016 / 16:41
 */
public class LeashExecutor {

    private static Map<LivingEntity, Integer> leashed = new HashMap<>();

    public static void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(CustomMobs.instance, LeashExecutor::doTick, 5, 5);
    }

    public static void doTick() {
        try {
            synchronized (LeashManager.leashMap) {
                lblUUIDLoop: for (UUID uuid : LeashManager.leashMap.keySet()) {
                    if(isLeashActive(uuid)) continue;
                    for (World world : Bukkit.getWorlds()) {
                        for (Entity e : world.getEntities()) {
                            if(e != null && e instanceof LivingEntity && e.getUniqueId().equals(uuid)) {
                                leashed.put((LivingEntity) e, 0);
                                continue lblUUIDLoop;
                            }
                        }
                    }
                }
            }
            int leashTolerance = CustomMobs.instance.getConfigHandler().leashViolationTolerance();
            Iterator<LivingEntity> iterator = leashed.keySet().iterator();
            while (iterator.hasNext()) {
                LivingEntity le = iterator.next();
                LeashManager.ActiveLeashInfo info = LeashManager.leashMap.get(le.getUniqueId());
                if (info == null) {
                    iterator.remove();
                    continue;
                }
                World eW = le.getWorld();
                Location leashCenter = info.to;
                if(!eW.equals(leashCenter.getWorld())) {
                    le.teleport(leashCenter);
                    continue;
                }
                double dst = le.getLocation().distance(info.to);
                if(dst > info.maxRange) {
                    int violationCounter = leashed.get(le);
                    if(violationCounter >= leashTolerance) {
                        leashed.put(le, 0);
                        le.teleport(info.to);
                    } else {
                        float perc = ((float) violationCounter) / ((float) leashTolerance);
                        Vector pushVec = info.to.toVector().clone().subtract(le.getLocation().toVector().clone()).multiply(perc);
                        le.setVelocity(pushVec);
                        leashed.put(le, leashed.get(le) + 1);
                    }
                } else {
                    leashed.put(le, 0);
                }
            }
        } catch (Exception exc) {}
    }

    public static void cutLeash(LivingEntity le) {
        leashed.remove(le);
    }

    public static void leash(LivingEntity le) {
        leashed.put(le, 0);
    }

    private static boolean isLeashActive(UUID uuid) {
        for (LivingEntity le : leashed.keySet()) {
            if(le.getUniqueId().equals(uuid)) return true;
        }
        return false;
    }

}

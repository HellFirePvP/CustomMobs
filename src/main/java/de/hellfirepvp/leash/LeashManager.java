package de.hellfirepvp.leash;

import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.file.read.LeashDataReader;
import de.hellfirepvp.file.write.LeashDataWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: LeashManager
 * Created by HellFirePvP
 * Date: 26.05.2016 / 16:41
 */
public class LeashManager {

    protected static final Map<UUID, ActiveLeashInfo> leashMap = new HashMap<>();
    protected static final List<LeashInfo> mobsToLeash = new ArrayList<>();

    public static void load() {
        reRead();
    }

    private static void reRead() {
        synchronized (leashMap) {
            synchronized (mobsToLeash) {
                leashMap.clear();
                mobsToLeash.clear();

                LeashDataReader.read(leashMap, mobsToLeash);
            }
        }
    }

    private static void write() {
        LeashDataWriter.writeToFile(leashMap, mobsToLeash);
    }

    private static LeashInfo getLeashInfo(String name) {
        synchronized (mobsToLeash) {
            for (LeashInfo info : mobsToLeash) {
                if(info.nameToLeash.equals(name)) return info;
            }
        }
        return null;
    }

    private static void appendNewLeash(UUID entityUUID, ActiveLeashInfo info) {
        synchronized (leashMap) {
            leashMap.put(entityUUID, info);
        }
        write();
        reRead();
    }

    private static void appendNewLeash(UUID entityUUID, Location to, double range) {
        appendNewLeash(entityUUID, new ActiveLeashInfo(to, range));
    }

    public static void addNewLeashSetting(String name, double maxRange) {
        if(shouldBeLeashed(name)) return;
        LeashInfo info = new LeashInfo(name, maxRange);
        synchronized (mobsToLeash) {
            mobsToLeash.add(info);
        }
        write();
        reRead();
    }

    public static boolean removeLeashSetting(String customMobName) {
        boolean success = false;
        synchronized (mobsToLeash) {
            Iterator<LeashInfo> it = mobsToLeash.iterator();
            while (it.hasNext()) {
                if(it.next().nameToLeash.equals(customMobName)) {
                    it.remove();
                    success = true;
                }
            }
        }
        if(success) {
            write();
            reRead();
        }
        return success;
    }

    public static boolean shouldBeLeashed(String customMobName) {
        return getLeashInfo(customMobName) != null;
    }

    public static boolean shouldBeLeashed(CustomMob mob) {
        return shouldBeLeashed(mob.getMobFileName());
    }

    public static void leash(UUID uuid, Location to, double maxRange, boolean initalSearch) {
        appendNewLeash(uuid, to, maxRange);
        if(initalSearch) {
            for (World world : Bukkit.getWorlds()) {
                for (Entity e : world.getEntities()) {
                    if(e != null && e instanceof LivingEntity && e.getUniqueId().equals(uuid)) {
                        LeashExecutor.leash((LivingEntity) e);
                        return;
                    }
                }
            }
        }
    }

    public static void leash(LivingEntity entity, Location to, double maxRange) {
        leash(entity.getUniqueId(), to, maxRange, false);
        LeashExecutor.leash(entity);
    }

    public static void leash(LivingEntity instance, CustomMob mob, Location toSpawnAt) {
        if(!shouldBeLeashed(mob)) return;
        LeashInfo info = getLeashInfo(mob.getMobFileName());
        if(info == null) return;
        leash(instance, toSpawnAt, info.maxLeashRange);
    }

    public static void leash(LivingEntity instance, CustomMob mob) {
        leash(instance, mob, instance.getLocation());
    }

    public static void unleash(LivingEntity entity) {
        synchronized (leashMap) {
            leashMap.remove(entity.getUniqueId());
        }
        LeashExecutor.cutLeash(entity);
        write();
    }

    public static class LeashInfo {

        public final String nameToLeash;
        public final double maxLeashRange;

        public LeashInfo(String nameToLeash, double maxLeashRange) {
            this.nameToLeash = nameToLeash;
            this.maxLeashRange = maxLeashRange;
        }
    }

    public static class ActiveLeashInfo {

        public final Location to;
        public final double maxRange;

        public ActiveLeashInfo(Location to, double maxRange) {
            this.to = to;
            this.maxRange = maxRange;
        }

    }

}

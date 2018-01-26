package de.hellfirepvp.leash;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.World;
import org.bukkit.Bukkit;
import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.Location;
import java.util.Iterator;
import de.hellfirepvp.file.write.LeashDataWriter;
import de.hellfirepvp.file.read.LeashDataReader;
import java.util.List;
import java.util.UUID;
import java.util.Map;

public class LeashManager
{
    protected static final Map<UUID, ActiveLeashInfo> leashMap;
    protected static final List<LeashInfo> mobsToLeash;
    
    public static void load() {
        reRead();
    }
    
    private static void reRead() {
        synchronized (LeashManager.leashMap) {
            synchronized (LeashManager.mobsToLeash) {
                LeashManager.leashMap.clear();
                LeashManager.mobsToLeash.clear();
                LeashDataReader.read(LeashManager.leashMap, LeashManager.mobsToLeash);
            }
        }
    }
    
    private static void write() {
        LeashDataWriter.writeToFile(LeashManager.leashMap, LeashManager.mobsToLeash);
    }
    
    private static LeashInfo getLeashInfo(final String name) {
        synchronized (LeashManager.mobsToLeash) {
            for (final LeashInfo info : LeashManager.mobsToLeash) {
                if (info.nameToLeash.equals(name)) {
                    return info;
                }
            }
        }
        return null;
    }
    
    private static void appendNewLeash(final UUID entityUUID, final ActiveLeashInfo info) {
        synchronized (LeashManager.leashMap) {
            LeashManager.leashMap.put(entityUUID, info);
        }
        write();
        reRead();
    }
    
    private static void appendNewLeash(final UUID entityUUID, final Location to, final double range) {
        appendNewLeash(entityUUID, new ActiveLeashInfo(to, range));
    }
    
    public static void addNewLeashSetting(final String name, final double maxRange) {
        if (shouldBeLeashed(name)) {
            return;
        }
        final LeashInfo info = new LeashInfo(name, maxRange);
        synchronized (LeashManager.mobsToLeash) {
            LeashManager.mobsToLeash.add(info);
        }
        write();
        reRead();
    }
    
    public static boolean removeLeashSetting(final String customMobName) {
        boolean success = false;
        synchronized (LeashManager.mobsToLeash) {
            final Iterator<LeashInfo> it = LeashManager.mobsToLeash.iterator();
            while (it.hasNext()) {
                if (it.next().nameToLeash.equals(customMobName)) {
                    it.remove();
                    success = true;
                }
            }
        }
        if (success) {
            write();
            reRead();
        }
        return success;
    }
    
    public static boolean shouldBeLeashed(final String customMobName) {
        return getLeashInfo(customMobName) != null;
    }
    
    public static boolean shouldBeLeashed(final CustomMob mob) {
        return shouldBeLeashed(mob.getMobFileName());
    }
    
    public static void leash(final UUID uuid, final Location to, final double maxRange, final boolean initalSearch) {
        appendNewLeash(uuid, to, maxRange);
        if (initalSearch) {
            for (final World world : Bukkit.getWorlds()) {
                for (final Entity e : world.getEntities()) {
                    if (e != null && e instanceof LivingEntity && e.getUniqueId().equals(uuid)) {
                        LeashExecutor.leash((LivingEntity)e);
                    }
                }
            }
        }
    }
    
    public static void leash(final LivingEntity entity, final Location to, final double maxRange) {
        leash(entity.getUniqueId(), to, maxRange, false);
        LeashExecutor.leash(entity);
    }
    
    public static void leash(final LivingEntity instance, final CustomMob mob, final Location toSpawnAt) {
        if (!shouldBeLeashed(mob)) {
            return;
        }
        final LeashInfo info = getLeashInfo(mob.getMobFileName());
        if (info == null) {
            return;
        }
        leash(instance, toSpawnAt, info.maxLeashRange);
    }
    
    public static void leash(final LivingEntity instance, final CustomMob mob) {
        leash(instance, mob, instance.getLocation());
    }
    
    public static void unleash(final LivingEntity entity) {
        synchronized (LeashManager.leashMap) {
            LeashManager.leashMap.remove(entity.getUniqueId());
        }
        LeashExecutor.cutLeash(entity);
        write();
    }
    
    static {
        leashMap = new HashMap<UUID, ActiveLeashInfo>();
        mobsToLeash = new ArrayList<LeashInfo>();
    }
    
    public static class LeashInfo
    {
        public final String nameToLeash;
        public final double maxLeashRange;
        
        public LeashInfo(final String nameToLeash, final double maxLeashRange) {
            this.nameToLeash = nameToLeash;
            this.maxLeashRange = maxLeashRange;
        }
    }
    
    public static class ActiveLeashInfo
    {
        public final Location to;
        public final double maxRange;
        
        public ActiveLeashInfo(final Location to, final double maxRange) {
            this.to = to;
            this.maxRange = maxRange;
        }
    }
}

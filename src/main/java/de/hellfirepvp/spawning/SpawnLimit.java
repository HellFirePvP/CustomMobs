package de.hellfirepvp.spawning;

import de.hellfirepvp.data.mob.CustomMob;
import java.util.Iterator;
import org.bukkit.entity.Entity;
import org.bukkit.World;
import org.bukkit.Bukkit;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.LivingEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpawnLimit
{
    private Map<String, Integer> fixedLimit;
    private List<String> noLimit;
    private Map<String, List<Integer>> currentlyActive;
    
    public SpawnLimit() {
        this.fixedLimit = new HashMap<String, Integer>();
        this.noLimit = new ArrayList<String>();
        this.currentlyActive = new HashMap<String, List<Integer>>();
    }
    
    public boolean spawnedIncrement(final String mobName, final LivingEntity entity) {
        if (!this.canSpawn(mobName)) {
            CustomMobs.logger.severe("Call to .increment eventhough it wasn't allowed for " + mobName + "!");
            return false;
        }
        if (!this.currentlyActive.containsKey(mobName)) {
            this.currentlyActive.put(mobName, new ArrayList<Integer>());
        }
        if (!this.currentlyActive.get(mobName).contains(entity.getEntityId())) {
            this.currentlyActive.get(mobName).add(entity.getEntityId());
        }
        return true;
    }
    
    public boolean decrement(final String mobName, final LivingEntity entity) {
        if (!this.currentlyActive.containsKey(mobName)) {
            this.currentlyActive.put(mobName, new ArrayList<Integer>());
        }
        return this.currentlyActive.get(mobName).contains(entity.getEntityId()) && this.currentlyActive.get(mobName).remove((Object)entity.getEntityId());
    }
    
    public boolean canSpawn(final String mobName) {
        if (this.noLimit.contains(mobName)) {
            return true;
        }
        if (this.fixedLimit.get(mobName) == null) {
            return true;
        }
        if (!this.currentlyActive.containsKey(mobName)) {
            this.currentlyActive.put(mobName, new ArrayList<Integer>());
        }
        final int limit = this.fixedLimit.get(mobName);
        final int alive = this.currentlyActive.get(mobName).size();
        return alive + 1 <= limit;
    }
    
    private void flush() {
        this.fixedLimit.clear();
        this.noLimit.clear();
        this.currentlyActive.clear();
        for (final World w : Bukkit.getWorlds()) {
            for (final Entity e : w.getEntities()) {
                for (final List<Integer> ids : this.currentlyActive.values()) {
                    if (ids.contains(e.getEntityId())) {
                        e.remove();
                        break;
                    }
                }
            }
        }
    }
    
    public void loadData() {
        this.flush();
        CustomMobs.instance.getMobDataHolder().getAllLoadedMobs().forEach(mob -> this.reloadMob(mob.getMobFileName(), mob.getDataAdapter().getSpawnLimit()));
    }
    
    public void reloadSingleMob(final String name, final int newSpawnLimit) {
        CustomMob.killAll(name);
        this.fixedLimit.remove(name);
        this.noLimit.remove(name);
        this.currentlyActive.remove(name);
        this.reloadMob(name, newSpawnLimit);
    }
    
    public void reloadMob(final String name, final int newSpawnLimit) {
        if (newSpawnLimit > -1) {
            this.fixedLimit.put(name, newSpawnLimit);
        }
        else {
            this.noLimit.add(name);
        }
        this.currentlyActive.put(name, new ArrayList<Integer>());
    }
}

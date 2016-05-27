package de.hellfirepvp.spawning;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnLimit
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:02
 */
public final class SpawnLimit {

    private Map<CustomMob, Integer> fixedLimit = new HashMap<>();
    private List<CustomMob> noLimit = new ArrayList<>();

    private Map<CustomMob, List<Integer>> currentlyActive = new HashMap<>();

    //CHECK IF YOU CHECKED FOR ALLOWED OF SPAWNING BEFORE DOING THAT
    public boolean spawnedIncrement(CustomMob mob, LivingEntity entity) {
        if(!canSpawn(mob)) {
            CustomMobs.logger.severe("Call to .increment eventhough it wasn't allowed for " + mob.getMobFileName() + "!");
            return false;
        }
        if(!currentlyActive.containsKey(mob)) {
            currentlyActive.put(mob, new ArrayList<>());
        }
        if(!currentlyActive.get(mob).contains(entity.getEntityId())) {
            currentlyActive.get(mob).add(entity.getEntityId());
        }
        return true;
    }

    public boolean decrement(CustomMob mob, LivingEntity entity) {
        if(!currentlyActive.containsKey(mob)) {
            currentlyActive.put(mob, new ArrayList<>());
        }
        if(!currentlyActive.get(mob).contains(Integer.valueOf(entity.getEntityId())))
            return false;
        return currentlyActive.get(mob).remove(Integer.valueOf(entity.getEntityId()));
    }

    public boolean canSpawn(CustomMob mob) {
        if(noLimit.contains(mob)) {
            return true; //FREE
        }
        if(fixedLimit.get(mob) == null) {
            return true; //Not registered -> FREE
        }

        if(!currentlyActive.containsKey(mob)) {
            currentlyActive.put(mob, new ArrayList<>());
        }

        int limit = fixedLimit.get(mob); //Max. allowed. 0, 1, ... ,n
        int alive = currentlyActive.get(mob).size();
        return alive + 1 <= limit;
    }

    private void flush() {
        fixedLimit.clear();
        noLimit.clear();
        currentlyActive.clear();
        for(World w : Bukkit.getWorlds()) {
            for(Entity e : w.getEntities()) {
                for(List<Integer> ids : currentlyActive.values()) {
                    if(ids.contains(e.getEntityId())) {
                        e.remove();
                        break;
                    }
                }
            }
        }
    }

    public void loadData() {
        flush();

        CustomMobs.instance.getMobDataHolder().getAllLoadedMobs().forEach(this::reloadMob);
    }

    public void reloadSingleMob(CustomMob mob) {
        CustomMob.killAll(mob);

        fixedLimit.remove(mob);
        noLimit.remove(mob);
        currentlyActive.remove(mob);

        reloadMob(mob);
    }

    public void reloadMob(CustomMob mob) {
        int limit = mob.getDataAdapter().getSpawnLimit();
        if(limit > -1) {
            fixedLimit.put(mob, limit);
        } else {
            noLimit.add(mob);
        }
        currentlyActive.put(mob, new ArrayList<>());
    }

}

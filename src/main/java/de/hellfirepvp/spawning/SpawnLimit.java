package de.hellfirepvp.spawning;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.ICustomMob;
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

    private Map<String, Integer> fixedLimit = new HashMap<>();
    private List<String> noLimit = new ArrayList<>();

    private Map<String, List<Integer>> currentlyActive = new HashMap<>();

    //CHECK IF YOU CHECKED FOR ALLOWED OF SPAWNING BEFORE DOING THAT
    public boolean spawnedIncrement(String mobName, LivingEntity entity) {
        if(!canSpawn(mobName)) {
            CustomMobs.logger.severe("Call to .increment eventhough it wasn't allowed for " + mobName + "!");
            return false;
        }
        if(!currentlyActive.containsKey(mobName)) {
            currentlyActive.put(mobName, new ArrayList<>());
        }
        if(!currentlyActive.get(mobName).contains(entity.getEntityId())) {
            currentlyActive.get(mobName).add(entity.getEntityId());
        }
        return true;
    }

    public boolean decrement(String mobName, LivingEntity entity) {
        if(!currentlyActive.containsKey(mobName)) {
            currentlyActive.put(mobName, new ArrayList<>());
        }
        if(!currentlyActive.get(mobName).contains(Integer.valueOf(entity.getEntityId())))
            return false;
        return currentlyActive.get(mobName).remove(Integer.valueOf(entity.getEntityId()));
    }

    public boolean canSpawn(String mobName) {
        if(noLimit.contains(mobName)) {
            return true; //FREE
        }
        if(fixedLimit.get(mobName) == null) {
            return true; //Not registered -> FREE
        }

        if(!currentlyActive.containsKey(mobName)) {
            currentlyActive.put(mobName, new ArrayList<>());
        }

        int limit = fixedLimit.get(mobName); //Max. allowed. 0, 1, ... ,n
        int alive = currentlyActive.get(mobName).size();
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

        CustomMobs.instance.getMobDataHolder().getAllLoadedMobs().forEach(mob -> reloadMob(mob.getMobFileName(), mob.getDataAdapter().getSpawnLimit()));
    }

    public void reloadSingleMob(String name, int newSpawnLimit) {
        CustomMob.killAll(name);

        fixedLimit.remove(name);
        noLimit.remove(name);
        currentlyActive.remove(name);

        reloadMob(name, newSpawnLimit);
    }

    public void reloadMob(String name, int newSpawnLimit) {
        if(newSpawnLimit > -1) {
            fixedLimit.put(name, newSpawnLimit);
        } else {
            noLimit.add(name);
        }
        currentlyActive.put(name, new ArrayList<>());
    }

}

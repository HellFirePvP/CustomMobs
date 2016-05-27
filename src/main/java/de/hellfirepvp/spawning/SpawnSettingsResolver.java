package de.hellfirepvp.spawning;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.SpawnSettingsHolder;
import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HellFirePvP@Admin
 * Date: 30.04.2015 / 22:38
 * on Project CustomMobs
 * SpawnSettingsResolver
 */
public final class SpawnSettingsResolver {

    public boolean areThereMobs;

    public Map<Biome, List<String>> biomeBound = new HashMap<>();
    public Map<String, List<String>> worldBound = new HashMap<>(), regionBound = new HashMap<>();

    public List<String> undefBiome = new ArrayList<>(), undefWorld = new ArrayList<>(), undefRegion = new ArrayList<>();

    public void resolveSpawnSettings() {
        undefBiome.clear();
        undefWorld.clear();
        undefRegion.clear(); //No Operation
        biomeBound.clear();
        worldBound.clear();
        regionBound.clear(); //No Operation

        SpawnSettingsHolder holder = CustomMobs.instance.getSpawnSettings();

        areThereMobs = !holder.getMobNamesWithSettings().isEmpty();

        for(String mobName : holder.getMobNamesWithSettings()) {
            SpawnSettingsHolder.SpawnSettings settings = holder.getSettings(mobName);
            CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
            if(mob == null) continue; //Settings for no mob? GTFO

            addToList(settings.biomeSpecified, settings.biomes, biomeBound, undefBiome, mob.getMobFileName());
            addToList(settings.worldsSpecified, settings.worlds, worldBound, undefWorld, mob.getMobFileName());
            addToList(settings.regionsSpecified, settings.regions, regionBound, undefRegion, mob.getMobFileName());
        }

    }

    private <I, V> void addToList(boolean condition, Collection<I> iteratingCollection, Map<I, List<V>> boundMap, List<V> condFalseAdd, V toTryAdd) {
        if(condition) {
            for(I it : iteratingCollection) {
                if(boundMap.containsKey(it)) {
                    List<V> values = boundMap.get(it);
                    if(!values.contains(toTryAdd)) values.add(toTryAdd);
                } else {
                    List<V> list = new ArrayList<>();
                    list.add(toTryAdd);
                    boundMap.put(it, list);
                }
            }
        } else {
            if(!condFalseAdd.contains(toTryAdd)) {
                condFalseAdd.add(toTryAdd);
            }
        }
    }

}

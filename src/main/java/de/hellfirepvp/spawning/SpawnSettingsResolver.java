package de.hellfirepvp.spawning;

import de.hellfirepvp.data.mob.CustomMob;
import java.util.Iterator;
import de.hellfirepvp.data.SpawnSettingsHolder;
import java.util.Collection;
import de.hellfirepvp.CustomMobs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.block.Biome;
import java.util.Map;

public final class SpawnSettingsResolver
{
    public boolean areThereMobs;
    public Map<Biome, List<String>> biomeBound;
    public Map<String, List<String>> worldBound;
    public Map<String, List<String>> regionBound;
    public List<String> undefBiome;
    public List<String> undefWorld;
    public List<String> undefRegion;
    
    public SpawnSettingsResolver() {
        this.biomeBound = new HashMap<Biome, List<String>>();
        this.worldBound = new HashMap<String, List<String>>();
        this.regionBound = new HashMap<String, List<String>>();
        this.undefBiome = new ArrayList<String>();
        this.undefWorld = new ArrayList<String>();
        this.undefRegion = new ArrayList<String>();
    }
    
    public void resolveSpawnSettings() {
        this.undefBiome.clear();
        this.undefWorld.clear();
        this.undefRegion.clear();
        this.biomeBound.clear();
        this.worldBound.clear();
        this.regionBound.clear();
        final SpawnSettingsHolder holder = CustomMobs.instance.getSpawnSettings();
        this.areThereMobs = !holder.getMobNamesWithSettings().isEmpty();
        for (final String mobName : holder.getMobNamesWithSettings()) {
            final SpawnSettingsHolder.SpawnSettings settings = holder.getSettings(mobName);
            final CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
            if (mob == null) {
                continue;
            }
            this.addToList(settings.biomeSpecified, settings.biomes, this.biomeBound, this.undefBiome, mob.getMobFileName());
            this.addToList(settings.worldsSpecified, settings.worlds, this.worldBound, this.undefWorld, mob.getMobFileName());
            this.addToList(settings.regionsSpecified, settings.regions, this.regionBound, this.undefRegion, mob.getMobFileName());
        }
    }
    
    private <I, V> void addToList(final boolean condition, final Collection<I> iteratingCollection, final Map<I, List<V>> boundMap, final List<V> condFalseAdd, final V toTryAdd) {
        if (condition) {
            for (final I it : iteratingCollection) {
                if (boundMap.containsKey(it)) {
                    final List<V> values = boundMap.get(it);
                    if (values.contains(toTryAdd)) {
                        continue;
                    }
                    values.add(toTryAdd);
                }
                else {
                    final List<V> list = new ArrayList<V>();
                    list.add(toTryAdd);
                    boundMap.put(it, list);
                }
            }
        }
        else if (!condFalseAdd.contains(toTryAdd)) {
            condFalseAdd.add(toTryAdd);
        }
    }
}

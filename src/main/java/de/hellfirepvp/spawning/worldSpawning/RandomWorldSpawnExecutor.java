package de.hellfirepvp.spawning.worldSpawning;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.event.CustomMobSpawnEvent;
import de.hellfirepvp.data.SpawnSettingsHolder;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.MobDataHolder;
import de.hellfirepvp.integration.IntegrationHandler;
import de.hellfirepvp.spawning.SpawnLimit;
import de.hellfirepvp.spawning.SpawnLimitException;
import de.hellfirepvp.spawning.SpawnSettingsResolver;
import de.hellfirepvp.util.LocationUtils;
import de.hellfirepvp.util.WeightedRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: RandomWorldSpawnExecutor
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:01
 */
public final class RandomWorldSpawnExecutor {

    public static final Random RANDOM = new Random();

    private SpawnSettingsResolver buffer;

    public void loadData() {
        buffer = new SpawnSettingsResolver();
        buffer.resolveSpawnSettings();
    }

    //Returns the count of how many mobs spawned.
    public int handleMobSpawning(Location location) {
        if(IntegrationHandler.integrationFactions != null &&
                !IntegrationHandler.integrationFactions.isMobSpawningAllowed(location)) {
            return 0;
        }

        List<String> possibleSpawns = collectPossibleSpawnableMobs(location);
        if(possibleSpawns.isEmpty()) return 0;
        List<CustomMob> possibleMobs = evaluateMobs(possibleSpawns);
        if(possibleMobs.isEmpty()) return 0;
        possibleMobs = subtractLimited(possibleMobs); //Limited checked.
        if(possibleMobs.isEmpty()) return 0;

        if(IntegrationHandler.integrationWorldGuard != null) {
            Iterator<CustomMob> iterator = possibleMobs.iterator();
            while (iterator.hasNext()) {
                CustomMob mob = iterator.next();
                if(!IntegrationHandler.integrationWorldGuard.doRegionsAllowSpawning(mob.getEntityAdapter().getEntityType(), location)) {
                    iterator.remove();
                }
            }

            if(possibleMobs.isEmpty()) return 0;
        }

        Map<CustomMob, Double> mobWeights = new HashMap<>();
        SpawnSettingsHolder settingsHolder = CustomMobs.instance.getSpawnSettings();
        for(CustomMob mob : possibleMobs) {
            SpawnSettingsHolder.SpawnSettings settings = settingsHolder.getSettings(mob.getMobFileName());
            if(settings != null) mobWeights.put(mob, settings.spawnRate);
        }

        SpawnLimit limiter = CustomMobs.instance.getSpawnLimiter();

        CustomMob selected = WeightedRandom.getWeightedRandomChoice(mobWeights);
        SpawnSettingsHolder.SpawnSettings settings = settingsHolder.getSettings(selected.getMobFileName());

        int count = 0;
        LivingEntity entity;
        try {
            entity = selected.spawnAt(location);
        } catch (SpawnLimitException ignored) {
            return count;
        }

        CustomMobSpawnEvent event = new CustomMobSpawnEvent(selected, entity, CustomMobSpawnEvent.SpawnReason.CCONFIG);
        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()) {
            if(entity != null) {
                entity.remove();
                CustomMobs.instance.getSpawnLimiter().decrement(selected, entity);
            }
            return 0;
        }
        count++;

        if(settings.groupSpawn) {
            int groupCount = RANDOM.nextInt(settings.groupAmount);
            for (int i = 0; i < groupCount; i++) {
                if(!limiter.canSpawn(selected)) {
                    break;
                }
                LivingEntity spawned;
                try {
                    spawned = selected.spawnAt(LocationUtils.toRand(location, 5));
                } catch (SpawnLimitException ignored) {
                    break;
                }

                CustomMobSpawnEvent spawnEvent = new CustomMobSpawnEvent(selected, spawned, CustomMobSpawnEvent.SpawnReason.CCONFIG_GROUP);
                Bukkit.getPluginManager().callEvent(spawnEvent);

                if(spawnEvent.isCancelled()) {
                    if(spawned != null) {
                        spawned.remove();
                        CustomMobs.instance.getSpawnLimiter().decrement(selected, spawned);
                    }
                    continue;
                }

                count++;
            }
        }

        return count;
    }

    public List<CustomMob> evaluateMobs(List<String> possibleSpawns) {
        List<CustomMob> toReturn = new ArrayList<>();
        MobDataHolder mobHolder = CustomMobs.instance.getMobDataHolder();
        for(String mobName : possibleSpawns) {
            CustomMob mob = mobHolder.getCustomMob(mobName);
            if(mob != null) toReturn.add(mob);
        }
        return toReturn;
    }

    public List<CustomMob> subtractLimited(List<CustomMob> possibleSpawns) {
        List<CustomMob> mobs = new ArrayList<>();
        SpawnLimit limiter = CustomMobs.instance.getSpawnLimiter();
        mobs.addAll(possibleSpawns.stream().filter(limiter::canSpawn).collect(Collectors.toList()));
        return mobs;
    }

    public List<String> collectPossibleSpawnableMobs(Location location) {
        List<String> possible = new ArrayList<>();
        String worldN = location.getWorld().getName();
        Biome biome = location.getBlock().getBiome();
        List<String> regions;
        if(IntegrationHandler.integrationWorldGuard != null) {
            regions = IntegrationHandler.integrationWorldGuard.getRegionNames(location);
        } else {
            regions = new ArrayList<>();
        }

        List<String> allowedThisBiome = new ArrayList<>(buffer.undefBiome);
        List<String> thisBiomeOnly = buffer.biomeBound.get(biome);
        if(thisBiomeOnly != null) allowedThisBiome.addAll(thisBiomeOnly);

        List<String> allowedThisWorld = new ArrayList<>(buffer.undefWorld);
        List<String> thisWorldOnly = buffer.worldBound.get(worldN);
        if(thisWorldOnly != null) allowedThisWorld.addAll(thisWorldOnly);

        List<String> allowedThisRegion = new ArrayList<>(buffer.undefRegion);
        List<String> thisRegionOnly = new ArrayList<>();
        regions.stream().filter(buffer.regionBound::containsKey)
                .forEach(region -> buffer.regionBound.get(region).stream().filter(mob -> !thisRegionOnly.contains(mob))
                        .forEach(thisRegionOnly::add));
        thisRegionOnly.stream().filter(regionOnly -> !allowedThisRegion.contains(regionOnly)).forEach(allowedThisRegion::add);


        //World > biome > region

        //For each mob that is allowed to spawn in this world
        possible.addAll(allowedThisWorld.stream()
                .filter(mobName -> allowedThisBiome.contains(mobName) && allowedThisRegion.contains(mobName))
                .collect(Collectors.toList()));
        return possible;
    }

    public boolean shouldSpawnCustomMobNext() {
        /*
        Examples:
        Frequency 100 always true.
        Frequency 0 always false because it's + 1
        Rest is obvious
         */
        return (buffer.areThereMobs && CustomMobs.instance.getConfigHandler().spawnFrequency() > RANDOM.nextInt(100));
    }

}

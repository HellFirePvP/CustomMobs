package de.hellfirepvp.spawning;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.event.CustomMobSpawnEvent;
import de.hellfirepvp.api.exception.SpawnLimitException;
import de.hellfirepvp.data.SpawnerDataHolder;
import de.hellfirepvp.integration.IntegrationHandler;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.util.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnerHandler
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:01
 */
public final class SpawnerHandler {

    public static final Random RANDOM = new Random();

    private SpawnerDataHolder spawnerData;

    private Map<Location, Integer> actualDelay = new HashMap<>();

    public void start() {
        spawnerData = CustomMobs.instance.getSpawnerDataHolder();

        Bukkit.getScheduler().runTaskTimer(CustomMobs.instance, SpawnerHandler.this::doTick, 20L, 20L);
    }

    public Integer getRemainingDelay(Location l) {
        return actualDelay.get(l);
    }

    private void doTick() {
        Map<Location, SpawnerDataHolder.Spawner> data = spawnerData.getSpawners();
        for(Location l : data.keySet()) {
            SpawnerDataHolder.Spawner spawner = data.get(l);
            if(!actualDelay.containsKey(l)) {
                actualDelay.put(l, spawner.hasFixedDelay ? (spawner.fixedDelay > 0 ? spawner.fixedDelay : 1) : genDelay());
            } else {
                int delay = actualDelay.get(l);
                delay--;
                if(delay <= 0) {
                    actualDelay.put(l, spawner.hasFixedDelay ? (spawner.fixedDelay > 0 ? spawner.fixedDelay : 1) : genDelay());

                    handleSpawn(l, spawner);
                } else {
                    actualDelay.put(l, delay);
                }
            }
        }
    }

    private void handleSpawn(Location l, SpawnerDataHolder.Spawner spawner) {
        if(!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) return;

        if(!NMSReflector.nmsUtils.isPlayerInRange(l, CustomMobs.instance.getConfigHandler().spawnerRange())) return; //No players near.
        ICustomMob mob = spawner.linked;
        int count = 0;
        if(RANDOM.nextBoolean()) count += 2;
        if(RANDOM.nextBoolean()) count += 2;
        count += RANDOM.nextInt(count + 1);
        l.getWorld().playEffect(l, Effect.MOBSPAWNER_FLAMES, 0);
        for (int i = 0; i < count; i++) {

            Location spawnLoc = LocationUtils.toRand(l, 5); //Larger than default.

            if(IntegrationHandler.integrationWorldGuard != null) {
                if(!IntegrationHandler.integrationWorldGuard.doRegionsAllowSpawning(mob.getEntityType(), spawnLoc)) {
                    continue;
                }
            }

            LivingEntity entity;
            try {
                entity = mob.spawnAt(spawnLoc);
            } catch (SpawnLimitException ignored) {
                break;
            }

            CustomMobSpawnEvent event = new CustomMobSpawnEvent(mob, entity, l.getBlock());

            if(event.isCancelled()) {
                if(entity != null) {
                    entity.remove();
                    CustomMobs.instance.getSpawnLimiter().decrement(mob.getName(), entity);
                }
                continue;
            }

            l.getWorld().playEffect(entity.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
        }
    }

    private int genDelay() {
        return 20 + RANDOM.nextInt(10);
    }

}

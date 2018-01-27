package de.hellfirepvp.spawning;

import org.bukkit.entity.LivingEntity;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.event.CustomMobSpawnEvent;
import de.hellfirepvp.api.exception.SpawnLimitException;
import de.hellfirepvp.integration.IntegrationHandler;
import de.hellfirepvp.util.LocationUtils;
import org.bukkit.Effect;
import java.util.Iterator;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import de.hellfirepvp.CustomMobs;
import java.util.HashMap;
import org.bukkit.Location;
import java.util.Map;
import de.hellfirepvp.data.SpawnerDataHolder;
import java.util.Random;

public final class SpawnerHandler
{
    public static final Random RANDOM;
    private SpawnerDataHolder spawnerData;
    private Map<Location, Integer> actualDelay;
    
    public SpawnerHandler() {
        this.actualDelay = new HashMap<Location, Integer>();
    }
    
    public void start() {
        this.spawnerData = CustomMobs.instance.getSpawnerDataHolder();
        Bukkit.getScheduler().runTaskTimer((Plugin)CustomMobs.instance, this::doTick, 20L, 20L);
    }
    
    public Integer getRemainingDelay(final Location l) {
        return this.actualDelay.get(l);
    }
    
    private void doTick() {
        final Map<Location, SpawnerDataHolder.Spawner> data = this.spawnerData.getSpawners();
        for (final Location l : data.keySet()) {
            final SpawnerDataHolder.Spawner spawner = data.get(l);
            if (!this.actualDelay.containsKey(l)) {
                this.actualDelay.put(l, spawner.hasFixedDelay ? ((spawner.fixedDelay > 0) ? spawner.fixedDelay : 1) : this.genDelay());
            }
            else {
                int delay = this.actualDelay.get(l);
                if (--delay <= 0) {
                    if (NMSReflector.nmsUtils.isPlayerInRange(l, CustomMobs.instance.getConfigHandler().spawnerRange())) {
                        this.actualDelay.put(l, spawner.hasFixedDelay ? ((spawner.fixedDelay > 0) ? spawner.fixedDelay : 1) : this.genDelay());
                        this.handleSpawn(l, spawner);
                    }
                    else {
                        this.actualDelay.put(l, 1);
                    }
                }
                else {
                    this.actualDelay.put(l, delay);
                }
            }
        }
    }
    
    private void handleSpawn(final Location l, final SpawnerDataHolder.Spawner spawner) {
        if (!l.getWorld().isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4)) {
            return;
        }
        final ICustomMob mob = spawner.linked;
        int count = 0;
        if (SpawnerHandler.RANDOM.nextBoolean()) {
            count += 2;
        }
        if (SpawnerHandler.RANDOM.nextBoolean()) {
            count += 2;
        }
        count += SpawnerHandler.RANDOM.nextInt(count + 1);
        l.getWorld().playEffect(l, Effect.MOBSPAWNER_FLAMES, 0);
        for (int i = 0; i < count; ++i) {
            final Location spawnLoc = LocationUtils.toRand(l, 5);
            if (IntegrationHandler.integrationWorldGuard == null || IntegrationHandler.integrationWorldGuard.doRegionsAllowSpawning(mob.getEntityType(), spawnLoc)) {
                LivingEntity entity;
                try {
                    entity = mob.spawnAt(spawnLoc);
                }
                catch (SpawnLimitException ignored) {
                    break;
                }
                final CustomMobSpawnEvent event = new CustomMobSpawnEvent(mob, entity, l.getBlock());
                if (event.isCancelled()) {
                    if (entity != null) {
                        entity.remove();
                        CustomMobs.instance.getSpawnLimiter().decrement(mob.getName(), entity);
                    }
                }
                else {
                    l.getWorld().playEffect(entity.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
                }
            }
        }
    }
    
    private int genDelay() {
        return 20 + SpawnerHandler.RANDOM.nextInt(10);
    }
    
    static {
        RANDOM = new Random();
    }
}

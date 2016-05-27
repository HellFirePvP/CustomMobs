package de.hellfirepvp.spawning;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.event.CustomMobSpawnEvent;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: Respawner
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:01
 */
public final class Respawner {

    private RespawnDataHolder respawnData;

    private Map<String, Long> respawning = new HashMap<>();

    public void start() {
        respawnData = CustomMobs.instance.getRespawnSettings();

        if(CustomMobs.instance.getConfigHandler().spawnRespawnMobsAtStartup()) {
            Bukkit.getScheduler().runTaskLater(CustomMobs.instance, () -> {
                Map<String, RespawnDataHolder.RespawnSettings> data = respawnData.getRespawnData();
                for(String mobName : data.keySet()) {
                    RespawnDataHolder.RespawnSettings settings = data.get(mobName);
                    CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
                    if(mob == null) continue;

                    LivingEntity entity;
                    try {
                        entity = mob.spawnAt(settings.location);
                    } catch (SpawnLimitException ignored) {
                        continue;
                    }

                    CustomMobSpawnEvent event = new CustomMobSpawnEvent(mob, entity, CustomMobSpawnEvent.SpawnReason.RESPAWN);
                    Bukkit.getPluginManager().callEvent(event);

                    if(event.isCancelled()) {
                        if(entity != null) {
                            entity.remove();
                            CustomMobs.instance.getSpawnLimiter().decrement(mob, entity);
                        }
                    }
                }
            }, CustomMobs.instance.getConfigHandler().spawnRespawnMobsAtStartupDelay());
        }

        Bukkit.getScheduler().runTaskTimer(CustomMobs.instance, this::doTick, 20L, 20L);
    }

    private void doTick() {
        for(String mobName : respawning.keySet()) {
            Long val = respawning.get(mobName);
            val--;
            if(val <= 0) {
                respawning.remove(mobName);

                CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
                if(mob == null) continue;

                RespawnDataHolder.RespawnSettings settings = respawnData.getSettings(mobName);
                if(settings == null) continue;

                LivingEntity entity;
                try {
                    entity = mob.spawnAt(settings.location);
                } catch (SpawnLimitException ignored) {
                    continue;
                }

                CustomMobSpawnEvent event = new CustomMobSpawnEvent(mob, entity, CustomMobSpawnEvent.SpawnReason.RESPAWN);
                Bukkit.getPluginManager().callEvent(event);

                if(event.isCancelled()) {
                    if(entity != null) {
                        entity.remove();
                        CustomMobs.instance.getSpawnLimiter().decrement(mob, entity);
                    }
                }
            } else {
                respawning.put(mobName, val);
            }
        }
    }

    public void informDeath(CustomMob mob) {
        if(respawnData.getSettings(mob.getMobFileName()) != null) {
            if(!respawning.containsKey(mob.getMobFileName())) {
                respawning.put(mob.getMobFileName(), respawnData.getSettings(mob.getMobFileName()).respawnTime);
            }
        }
    }

}

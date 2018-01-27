package de.hellfirepvp.event;

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.leash.LeashExecutor;
import org.bukkit.entity.LivingEntity;
import de.hellfirepvp.CustomMobs;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.Listener;

public class WorldEventListener implements Listener
{
    @EventHandler
    public void onChUnload(final ChunkUnloadEvent event) {
        if (!CustomMobs.instance.getConfigHandler().removeCustomMobOnChunkUnload()) {
            return;
        }
        if (!CustomMobs.instance.getConfigHandler().removeLimitedMobsOnChunkUnload()) {
            return;
        }
        final Entity[] entities;
        final Entity[] entites = entities = event.getChunk().getEntities();
        for (final Entity e : entities) {
            if (e != null) {
                if (e instanceof LivingEntity) {
                    final CustomMob mob = GeneralEventListener.checkEntity((LivingEntity)e);
                    if (mob == null) {
                        return;
                    }
                    LeashExecutor.cutLeash((LivingEntity)e);
                    CustomMob.kill(mob, (LivingEntity)e);
                    if (CustomMobs.instance.getConfigHandler().removeCustomMobOnChunkUnload()) {
                        e.remove();
                        CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), (LivingEntity)e);
                        LeashManager.unleash((LivingEntity)e);
                    }
                    else if (CustomMobs.instance.getConfigHandler().removeLimitedMobsOnChunkUnload() && mob.getDataAdapter().getSpawnLimit() > 0) {
                        CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), (LivingEntity)e);
                        e.remove();
                        LeashManager.unleash((LivingEntity)e);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onChLoad(final ChunkLoadEvent event) {
        if (!CustomMobs.instance.getConfigHandler().removeCustomMobOnChunkUnload()) {
            return;
        }
        if (!CustomMobs.instance.getConfigHandler().removeLimitedMobsOnChunkUnload()) {
            return;
        }
        final Entity[] entities;
        final Entity[] entites = entities = event.getChunk().getEntities();
        for (final Entity e : entities) {
            if (e != null) {
                if (e instanceof LivingEntity) {
                    final CustomMob mob = GeneralEventListener.checkEntity((LivingEntity)e);
                    if (mob == null) {
                        return;
                    }
                    LeashExecutor.cutLeash((LivingEntity)e);
                    if (CustomMobs.instance.getConfigHandler().removeCustomMobOnChunkUnload()) {
                        e.remove();
                        CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), (LivingEntity)e);
                        LeashManager.unleash((LivingEntity)e);
                    }
                    else if (CustomMobs.instance.getConfigHandler().removeLimitedMobsOnChunkUnload() && mob.getDataAdapter().getSpawnLimit() > 0) {
                        CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), (LivingEntity)e);
                        e.remove();
                        LeashManager.unleash((LivingEntity)e);
                    }
                }
            }
        }
    }
}

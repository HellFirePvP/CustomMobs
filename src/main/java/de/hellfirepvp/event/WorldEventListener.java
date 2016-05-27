package de.hellfirepvp.event;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.leash.LeashExecutor;
import de.hellfirepvp.leash.LeashManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WorldEventListener
 * Created by HellFirePvP
 * Date: 26.05.2016 / 17:34
 */
public class WorldEventListener implements Listener {

    @EventHandler
    public void onChUnload(ChunkUnloadEvent event) {
        if(!CustomMobs.instance.getConfigHandler().removeCustomMobOnChunkUnload()) return;
        if(!CustomMobs.instance.getConfigHandler().removeLimitedMobsOnChunkUnload()) return;
        Entity[] entites = event.getChunk().getEntities();
        for (Entity e : entites) {
            if (e == null) continue;
            if (!(e instanceof LivingEntity)) continue;
            CustomMob mob = GeneralEventListener.checkEntity((LivingEntity) e);

            if (mob == null) return; //Not our mob.

            //Updating SpawnLimit and Registry
            LeashExecutor.cutLeash((LivingEntity) e);
            CustomMob.kill(mob, (LivingEntity) e);
            if(CustomMobs.instance.getConfigHandler().removeCustomMobOnChunkUnload()) {
                e.remove();
                CustomMobs.instance.getSpawnLimiter().decrement(mob, (LivingEntity) e);
                LeashManager.unleash((LivingEntity) e);
            } else if(CustomMobs.instance.getConfigHandler().removeLimitedMobsOnChunkUnload()) {
                if (mob.getDataAdapter().getSpawnLimit() > 0) {
                    CustomMobs.instance.getSpawnLimiter().decrement(mob, (LivingEntity) e);
                    e.remove();
                    LeashManager.unleash((LivingEntity) e);
                }
            }
        }
    }

    @EventHandler
    public void onChLoad(ChunkLoadEvent event) {
        if(!CustomMobs.instance.getConfigHandler().removeCustomMobOnChunkUnload()) return;
        if(!CustomMobs.instance.getConfigHandler().removeLimitedMobsOnChunkUnload()) return;
        Entity[] entites = event.getChunk().getEntities();
        for (Entity e : entites) {
            if (e == null) continue;
            if (!(e instanceof LivingEntity)) continue;
            CustomMob mob = GeneralEventListener.checkEntity((LivingEntity) e);

            if (mob == null) return; //Not our mob.
            LeashExecutor.cutLeash((LivingEntity) e);
            if(CustomMobs.instance.getConfigHandler().removeCustomMobOnChunkUnload()) {
                e.remove();
                CustomMobs.instance.getSpawnLimiter().decrement(mob, (LivingEntity) e);
                LeashManager.unleash((LivingEntity) e);
            } else if (CustomMobs.instance.getConfigHandler().removeLimitedMobsOnChunkUnload() && mob.getDataAdapter().getSpawnLimit() > 0) {
                CustomMobs.instance.getSpawnLimiter().decrement(mob, (LivingEntity) e);
                e.remove();
                LeashManager.unleash((LivingEntity) e);
            }
        }
    }

}

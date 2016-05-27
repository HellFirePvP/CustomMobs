package de.hellfirepvp.api.event;

import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * HellFirePvP@Admin
 * Date: 13.05.2015 / 13:12
 * on Project CustomMobs
 * CustomMobSpawnEvent
 */
public class CustomMobSpawnEvent extends CustomMobEvent implements Cancellable {

    public static HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private LivingEntity entity = null;
    private Block spawner = null;
    private SpawnReason reason = SpawnReason.NULL;

    public CustomMobSpawnEvent(CustomMob mob, LivingEntity entity, SpawnReason reason) {
        super(mob);
        this.entity = entity;
        this.reason = reason;
    }
    public CustomMobSpawnEvent(CustomMob mob, LivingEntity entity, Block spawner) {
        super(mob);
        this.entity = entity;
        this.reason = SpawnReason.SPAWNER;
        this.spawner = spawner;
    }

    public boolean spawnedBySpawner() {
        return spawner != null;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Block getSpawner() {
        return spawner;
    }

    public SpawnReason getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    public static class SpawnReason {

        public static final SpawnReason COMMAND_CMOB = new SpawnReason("COMMAND_CMOB");
        public static final SpawnReason COMMAND_CCMOB = new SpawnReason("COMMAND_CCMOB");
        public static final SpawnReason SPAWNER = new SpawnReason("SPAWNER");
        public static final SpawnReason CCONFIG = new SpawnReason("CCONFIG");
        public static final SpawnReason CCONFIG_GROUP = new SpawnReason("CCONFIG_GROUP");
        public static final SpawnReason RESPAWN = new SpawnReason("RESPAWN");
        protected static final SpawnReason NULL = new SpawnReason("NULL");

        private String view;

        private SpawnReason(String view) {
            this.view = view;
        }

        public String toString() {
            return this.view;
        }
    }
}

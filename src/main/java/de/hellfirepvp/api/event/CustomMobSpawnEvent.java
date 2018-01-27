package de.hellfirepvp.api.event;

import de.hellfirepvp.api.data.ICustomMob;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

public class CustomMobSpawnEvent extends CustomMobEvent implements Cancellable
{
    public static HandlerList handlers;
    private boolean cancel;
    private LivingEntity entity;
    private Block spawner;
    private SpawnReason reason;
    
    public CustomMobSpawnEvent(final ICustomMob mob, final LivingEntity entity, final SpawnReason reason) {
        super(mob);
        this.cancel = false;
        this.entity = null;
        this.spawner = null;
        this.reason = SpawnReason.NULL;
        this.entity = entity;
        this.reason = reason;
    }
    
    public CustomMobSpawnEvent(final ICustomMob mob, final LivingEntity entity, final Block spawner) {
        super(mob);
        this.cancel = false;
        this.entity = null;
        this.spawner = null;
        this.reason = SpawnReason.NULL;
        this.entity = entity;
        this.reason = SpawnReason.SPAWNER;
        this.spawner = spawner;
    }
    
    public boolean spawnedBySpawner() {
        return this.spawner != null;
    }
    
    public LivingEntity getEntity() {
        return this.entity;
    }
    
    public Block getSpawner() {
        return this.spawner;
    }
    
    public SpawnReason getReason() {
        return this.reason;
    }
    
    @Override
    public HandlerList getHandlers() {
        return CustomMobSpawnEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return CustomMobSpawnEvent.handlers;
    }
    
    public boolean isCancelled() {
        return this.cancel;
    }
    
    public void setCancelled(final boolean b) {
        this.cancel = b;
    }
    
    static {
        CustomMobSpawnEvent.handlers = new HandlerList();
    }
    
    public static class SpawnReason
    {
        public static final SpawnReason COMMAND_CMOB;
        public static final SpawnReason COMMAND_CCMOB;
        public static final SpawnReason SPAWNER;
        public static final SpawnReason CSPAWN;
        public static final SpawnReason CSPAWN_GROUP;
        public static final SpawnReason RESPAWN;
        protected static final SpawnReason NULL;
        private String view;
        
        private SpawnReason(final String view) {
            this.view = view;
        }
        
        @Override
        public String toString() {
            return this.view;
        }
        
        static {
            COMMAND_CMOB = new SpawnReason("COMMAND_CMOB");
            COMMAND_CCMOB = new SpawnReason("COMMAND_CCMOB");
            SPAWNER = new SpawnReason("SPAWNER");
            CSPAWN = new SpawnReason("CSPAWN");
            CSPAWN_GROUP = new SpawnReason("CSPAWN_GROUP");
            RESPAWN = new SpawnReason("RESPAWN");
            NULL = new SpawnReason("NULL");
        }
    }
}

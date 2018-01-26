package de.hellfirepvp.api.data;

import java.util.Collection;
import javax.annotation.Nullable;
import de.hellfirepvp.api.data.callback.SpawnerDataCallback;
import org.bukkit.Location;

public interface ISpawnerEditor
{
    SpawnerDataCallback setSpawner(final ICustomMob p0, final Location p1);
    
    SpawnerDataCallback setSpawner(final ICustomMob p0, final Location p1, final Integer p2);
    
    SpawnerDataCallback resetSpawner(final Location p0);
    
    @Nullable
    SpawnerInfo getSpawner(final Location p0);
    
    Collection<SpawnerInfo> getAllSpawners();
    
    public static class SpawnerInfo
    {
        private final Location loc;
        private final ISpawner spawner;
        
        public SpawnerInfo(final Location loc, final ISpawner spawner) {
            this.loc = loc;
            this.spawner = spawner;
        }
        
        public Location getLocation() {
            return this.loc;
        }
        
        public ISpawner getSpawner() {
            return this.spawner;
        }
    }
    
    public interface ISpawner
    {
        ICustomMob getSpawningCustomMob();
        
        boolean hasFixedDelay();
        
        int getFixedDelay();
    }
}

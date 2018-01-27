package de.hellfirepvp.data;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.ISpawnerEditor;
import java.util.Collections;
import de.hellfirepvp.file.read.SpawnerDataReader;
import java.util.HashMap;
import org.bukkit.Location;
import java.util.Map;

public final class SpawnerDataHolder
{
    private Map<Location, Spawner> spawnerData;
    
    public SpawnerDataHolder() {
        this.spawnerData = new HashMap<Location, Spawner>();
    }
    
    public void loadData() {
        this.spawnerData.clear();
        SpawnerDataReader.readSpawnerData(this.spawnerData);
    }
    
    public Spawner getSpawnerAt(final Location location) {
        return this.spawnerData.get(location);
    }
    
    public Map<Location, Spawner> getSpawners() {
        return Collections.unmodifiableMap((Map<? extends Location, ? extends Spawner>)this.spawnerData);
    }
    
    public static class Spawner implements ISpawnerEditor.ISpawner
    {
        public final ICustomMob linked;
        public final int fixedDelay;
        public final boolean hasFixedDelay;
        
        public Spawner(final ICustomMob linked, final int fixedDelay, final boolean hasFixedDelay) {
            this.linked = linked;
            this.fixedDelay = fixedDelay;
            this.hasFixedDelay = hasFixedDelay;
        }
        
        @Override
        public ICustomMob getSpawningCustomMob() {
            return this.linked;
        }
        
        @Override
        public boolean hasFixedDelay() {
            return this.hasFixedDelay;
        }
        
        @Override
        public int getFixedDelay() {
            return this.fixedDelay;
        }
    }
}

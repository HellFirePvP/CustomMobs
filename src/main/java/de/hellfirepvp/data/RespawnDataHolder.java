package de.hellfirepvp.data;

import org.bukkit.Location;
import de.hellfirepvp.api.data.IRespawnEditor;
import java.util.Collections;
import de.hellfirepvp.file.read.RespawnDataReader;
import java.util.HashMap;
import java.util.Map;

public final class RespawnDataHolder
{
    private Map<String, RespawnSettings> respawnData;
    
    public RespawnDataHolder() {
        this.respawnData = new HashMap<String, RespawnSettings>();
    }
    
    public void loadData() {
        this.respawnData.clear();
        RespawnDataReader.loadData(this.respawnData);
    }
    
    public RespawnSettings getSettings(final String mobName) {
        return this.respawnData.get(mobName);
    }
    
    public Map<String, RespawnSettings> getRespawnData() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends RespawnSettings>)this.respawnData);
    }
    
    public static class RespawnSettings implements IRespawnEditor.IRespawnSettings
    {
        public final Location location;
        public final long respawnTime;
        
        public RespawnSettings(final Location location, final long respawnTime) {
            this.location = location;
            this.respawnTime = respawnTime;
        }
        
        @Override
        public Location getLocation() {
            return this.location;
        }
        
        @Override
        public long getRespawnDelay() {
            return this.respawnTime;
        }
    }
}

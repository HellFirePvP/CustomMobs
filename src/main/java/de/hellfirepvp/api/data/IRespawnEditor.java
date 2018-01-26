package de.hellfirepvp.api.data;

import java.util.Map;
import de.hellfirepvp.api.data.callback.RespawnDataCallback;
import org.bukkit.Location;

public interface IRespawnEditor
{
    RespawnDataCallback setRespawn(final ICustomMob p0, final Location p1, final long p2);
    
    RespawnDataCallback resetRespawn(final ICustomMob p0);
    
    Map<ICustomMob, IRespawnSettings> getRespawnSettings();
    
    public interface IRespawnSettings
    {
        Location getLocation();
        
        long getRespawnDelay();
    }
}

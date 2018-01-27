package de.hellfirepvp.data;

import java.util.Iterator;
import de.hellfirepvp.api.CustomMobsAPI;
import java.util.HashMap;
import de.hellfirepvp.CustomMobs;
import java.util.Map;
import de.hellfirepvp.file.write.RespawnDataWriter;
import de.hellfirepvp.api.data.callback.RespawnDataCallback;
import org.bukkit.Location;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.IRespawnEditor;

public class RespawnEditor implements IRespawnEditor
{
    @Override
    public RespawnDataCallback setRespawn(final ICustomMob mob, final Location location, final long delay) {
        return RespawnDataWriter.setRespawnSettings(mob.getName(), new RespawnDataHolder.RespawnSettings(location, delay));
    }
    
    @Override
    public RespawnDataCallback resetRespawn(final ICustomMob mob) {
        return RespawnDataWriter.resetRespawnSettings(mob.getName());
    }
    
    @Override
    public Map<ICustomMob, IRespawnSettings> getRespawnSettings() {
        final Map<String, RespawnDataHolder.RespawnSettings> settings = CustomMobs.instance.getRespawnSettings().getRespawnData();
        final Map<ICustomMob, IRespawnSettings> apiRespawnSettings = new HashMap<ICustomMob, IRespawnSettings>();
        for (final Map.Entry<String, RespawnDataHolder.RespawnSettings> entry : settings.entrySet()) {
            final ICustomMob mob = CustomMobsAPI.getCustomMob(entry.getKey());
            if (mob != null) {
                apiRespawnSettings.put(mob, entry.getValue());
            }
        }
        return apiRespawnSettings;
    }
}

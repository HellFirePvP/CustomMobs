package de.hellfirepvp.data;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.CustomMobsAPI;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.callback.RespawnDataCallback;
import de.hellfirepvp.file.write.RespawnDataWriter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: RespawnEditor
 * Created by HellFirePvP
 * Date: 30.05.2016 / 10:17
 */
public class RespawnEditor implements IRespawnEditor {

    @Override
    public RespawnDataCallback setRespawn(ICustomMob mob, Location location, long delay) {
        return RespawnDataWriter.setRespawnSettings(mob.getName(), new RespawnDataHolder.RespawnSettings(location, delay));
    }

    @Override
    public RespawnDataCallback resetRespawn(ICustomMob mob) {
        return RespawnDataWriter.resetRespawnSettings(mob.getName());
    }

    @Override
    public Map<ICustomMob, IRespawnSettings> getRespawnSettings() {
        Map<String, RespawnDataHolder.RespawnSettings> settings = CustomMobs.instance.getRespawnSettings().getRespawnData();
        Map<ICustomMob, IRespawnSettings> apiRespawnSettings = new HashMap<>();
        for (Map.Entry<String, RespawnDataHolder.RespawnSettings> entry : settings.entrySet()) {
            ICustomMob mob = CustomMobsAPI.getCustomMob(entry.getKey());
            if(mob != null) {
                apiRespawnSettings.put(mob, entry.getValue());
            }
        }
        return apiRespawnSettings;
    }

}

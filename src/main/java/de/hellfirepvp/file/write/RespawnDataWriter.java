package de.hellfirepvp.file.write;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Location;
import de.hellfirepvp.util.LocationUtils;
import de.hellfirepvp.data.RespawnDataHolder;
import org.bukkit.configuration.file.YamlConfiguration;
import de.hellfirepvp.CustomMobs;
import java.io.IOException;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.api.data.callback.RespawnDataCallback;

public class RespawnDataWriter
{
    public static RespawnDataCallback resetRespawnSettings(final String name) {
        final YamlConfiguration config = LibConfiguration.getRespawnSettingsConfiguration();
        if (!config.contains(name)) {
            return RespawnDataCallback.MOB_DOESNT_EXIST;
        }
        config.set(name, (Object)null);
        try {
            config.save(LibConfiguration.getRespawnSettingsFile());
        }
        catch (IOException ignored) {
            return RespawnDataCallback.IO_EXCEPTION;
        }
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getRespawnSettings().loadData();
        return RespawnDataCallback.SUCCESS;
    }
    
    public static RespawnDataCallback setRespawnSettings(final String name, final RespawnDataHolder.RespawnSettings settings) {
        final YamlConfiguration config = LibConfiguration.getRespawnSettingsConfiguration();
        if (config.contains(name)) {
            return RespawnDataCallback.MOB_ALREADY_EXISTS;
        }
        final Location toSave = settings.location;
        final String serializedLocation = LocationUtils.serializeExactLoc(toSave);
        final Long time = settings.respawnTime;
        final ConfigurationSection section = config.createSection(name);
        section.set("loc", (Object)serializedLocation);
        section.set("time", (Object)time);
        try {
            config.save(LibConfiguration.getRespawnSettingsFile());
        }
        catch (IOException ignored) {
            return RespawnDataCallback.IO_EXCEPTION;
        }
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getRespawnSettings().loadData();
        return RespawnDataCallback.SUCCESS;
    }
}

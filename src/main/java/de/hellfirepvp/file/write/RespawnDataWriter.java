package de.hellfirepvp.file.write;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.data.callback.RespawnDataCallback;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * HellFirePvP@Admin
 * Date: 10.05.2015 / 08:34
 * on Project CustomMobs
 * RespawnDataWriter
 */
public class RespawnDataWriter {

    public static RespawnDataCallback resetRespawnSettings(String name) {
        YamlConfiguration config = LibConfiguration.getRespawnSettingsConfiguration();

        if(!config.contains(name))
            return RespawnDataCallback.MOB_DOESNT_EXIST;

        config.set(name, null);

        try {
            config.save(LibConfiguration.getRespawnSettingsFile());
        } catch (IOException ignored) {
            return RespawnDataCallback.IO_EXCEPTION;
        }

        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getRespawnSettings().loadData();

        return RespawnDataCallback.SUCCESS;
    }

    public static RespawnDataCallback setRespawnSettings(String name, RespawnDataHolder.RespawnSettings settings) {
        YamlConfiguration config = LibConfiguration.getRespawnSettingsConfiguration();

        if(config.contains(name))
            return RespawnDataCallback.MOB_ALREADY_EXISTS;

        Location toSave = settings.location;
        String serializedLocation = LocationUtils.serializeExactLoc(toSave);
        Long time = settings.respawnTime;

        ConfigurationSection section = config.createSection(name);

        section.set(RESPAWN_DATA_LOCATION, serializedLocation);
        section.set(RESPAWN_DATA_RESPAWNTIME, time);

        try {
            config.save(LibConfiguration.getRespawnSettingsFile());
        } catch (IOException ignored) {
            return RespawnDataCallback.IO_EXCEPTION;
        }

        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getRespawnSettings().loadData();

        return RespawnDataCallback.SUCCESS;
    }

}

package de.hellfirepvp.file.write;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.api.data.callback.RespawnDataCallback;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: RespawnDataWriter
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:03
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

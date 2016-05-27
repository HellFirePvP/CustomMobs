package de.hellfirepvp.file.write;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.SpawnerDataHolder;
import de.hellfirepvp.data.callback.SpawnerDataCallback;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * HellFirePvP@Admin
 * Date: 10.05.2015 / 09:06
 * on Project CustomMobs
 * SpawnerDataWriter
 */
public class SpawnerDataWriter {

    public static SpawnerDataCallback resetSpawner(Location location) {
        YamlConfiguration config = LibConfiguration.getSpawnerDataConfiguration();
        String blockLoc = LocationUtils.serializeBlockLoc(location);

        if(!config.contains(blockLoc))
            return SpawnerDataCallback.LOCATION_NO_SPAWNER;

        config.set(blockLoc, null);

        try {
            config.save(LibConfiguration.getSpawnerDataFile());
        } catch (IOException ignored) {
            return SpawnerDataCallback.IO_EXCEPTION;
        }

        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnerDataHolder().loadData();

        return SpawnerDataCallback.SUCCESS;
    }

    public static SpawnerDataCallback setSpawner(Location location, SpawnerDataHolder.Spawner data) {
        YamlConfiguration config = LibConfiguration.getSpawnerDataConfiguration();
        String blockLoc = LocationUtils.serializeBlockLoc(location);

        if(config.contains(blockLoc))
            return SpawnerDataCallback.LOCATION_OCCUPIED;

        ConfigurationSection section = config.createSection(blockLoc);

        String mobName = data.linked.getMobFileName();

        section.set(SPAWNER_DATA_MOBNAME, mobName);
        section.set(SPAWNER_DATA_DELAY, data.hasFixedDelay ? data.fixedDelay : -1);

        try {
            config.save(LibConfiguration.getSpawnerDataFile());
        } catch (IOException ignored) {
            return SpawnerDataCallback.IO_EXCEPTION;
        }

        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnerDataHolder().loadData();

        return SpawnerDataCallback.SUCCESS;
    }

}

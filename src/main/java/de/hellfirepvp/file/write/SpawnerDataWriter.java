package de.hellfirepvp.file.write;

import org.bukkit.configuration.ConfigurationSection;
import de.hellfirepvp.data.SpawnerDataHolder;
import org.bukkit.configuration.file.YamlConfiguration;
import de.hellfirepvp.CustomMobs;
import java.io.IOException;
import de.hellfirepvp.util.LocationUtils;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.api.data.callback.SpawnerDataCallback;
import org.bukkit.Location;

public class SpawnerDataWriter
{
    public static SpawnerDataCallback resetSpawner(final Location location) {
        final YamlConfiguration config = LibConfiguration.getSpawnerDataConfiguration();
        final String blockLoc = LocationUtils.serializeBlockLoc(location);
        if (!config.contains(blockLoc)) {
            return SpawnerDataCallback.LOCATION_NO_SPAWNER;
        }
        config.set(blockLoc, (Object)null);
        try {
            config.save(LibConfiguration.getSpawnerDataFile());
        }
        catch (IOException ignored) {
            return SpawnerDataCallback.IO_EXCEPTION;
        }
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnerDataHolder().loadData();
        return SpawnerDataCallback.SUCCESS;
    }
    
    public static SpawnerDataCallback setSpawner(final Location location, final SpawnerDataHolder.Spawner data) {
        final YamlConfiguration config = LibConfiguration.getSpawnerDataConfiguration();
        final String blockLoc = LocationUtils.serializeBlockLoc(location);
        if (config.contains(blockLoc)) {
            return SpawnerDataCallback.LOCATION_OCCUPIED;
        }
        final ConfigurationSection section = config.createSection(blockLoc);
        final String mobName = data.linked.getName();
        section.set("mobname", (Object)mobName);
        section.set("delay", (Object)(data.hasFixedDelay ? data.fixedDelay : -1));
        try {
            config.save(LibConfiguration.getSpawnerDataFile());
        }
        catch (IOException ignored) {
            return SpawnerDataCallback.IO_EXCEPTION;
        }
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnerDataHolder().loadData();
        return SpawnerDataCallback.SUCCESS;
    }
}

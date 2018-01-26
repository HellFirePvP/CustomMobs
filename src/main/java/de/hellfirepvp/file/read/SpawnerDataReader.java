package de.hellfirepvp.file.read;

import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.util.LocationUtils;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.data.SpawnerDataHolder;
import org.bukkit.Location;
import java.util.Map;

public class SpawnerDataReader
{
    public static void readSpawnerData(final Map<Location, SpawnerDataHolder.Spawner> out) {
        final YamlConfiguration config = LibConfiguration.getSpawnerDataConfiguration();
        boolean changed = false;
        for (final String serializedLoc : config.getKeys(false)) {
            final Location loc = LocationUtils.deserializeBlockLoc(serializedLoc);
            if (loc == null) {
                CustomMobs.logger.info("Unable to deserialize spawner location: " + serializedLoc);
                CustomMobs.logger.info("Removing it from spawner specifications...");
                config.set(serializedLoc, (Object)null);
                changed = true;
            }
            else if (loc.getWorld() == null) {
                CustomMobs.logger.info("World for spawner location was not found: " + serializedLoc);
                CustomMobs.logger.info("Removing it from spawner specifications...");
                config.set(serializedLoc, (Object)null);
                changed = true;
            }
            else {
                final ConfigurationSection section = config.getConfigurationSection(serializedLoc);
                final int fixedDelay = section.getInt("delay", -1);
                final String mobName = section.getString("mobname", "undef");
                if (mobName.equals("undef")) {
                    config.set(serializedLoc, (Object)null);
                    changed = true;
                }
                else {
                    final CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
                    if (mob == null) {
                        config.set(serializedLoc, (Object)null);
                        changed = true;
                    }
                    else {
                        NMSReflector.nmsUtils.clearSpawner(loc.getBlock());
                        out.put(loc, new SpawnerDataHolder.Spawner(mob.createApiAdapter(), fixedDelay, fixedDelay > 0));
                    }
                }
            }
        }
        if (changed) {
            try {
                config.save(LibConfiguration.getSpawnerDataFile());
            }
            catch (IOException ex) {}
        }
    }
}

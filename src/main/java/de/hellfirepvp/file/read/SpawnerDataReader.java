package de.hellfirepvp.file.read;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.SpawnerDataHolder;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Map;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: SpawnerDataReader
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:03
 */
public class SpawnerDataReader {

    public static void readSpawnerData(Map<Location, SpawnerDataHolder.Spawner> out) {
        YamlConfiguration config = LibConfiguration.getSpawnerDataConfiguration();

        boolean changed = false;

        for(String serializedLoc : config.getKeys(false)) {
            Location loc = LocationUtils.deserializeBlockLoc(serializedLoc);
            if(loc == null) {
                config.set(serializedLoc, null);
                changed = true;
                continue;
            }

            ConfigurationSection section = config.getConfigurationSection(serializedLoc);

            int fixedDelay = section.getInt(SPAWNER_DATA_DELAY, -1);
            String mobName = section.getString(SPAWNER_DATA_MOBNAME, "undef");
            if(mobName.equals("undef")) {
                config.set(serializedLoc, null);
                changed = true;
                continue;
            }
            CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
            if(mob == null) {
                config.set(serializedLoc, null);
                changed = true;
                continue;
            }

            NMSReflector.nmsUtils.clearSpawner(loc.getBlock());

            out.put(loc, new SpawnerDataHolder.Spawner(mob, fixedDelay, fixedDelay > 0));
        }

        if(changed) {
            try {
                config.save(LibConfiguration.getSpawnerDataFile());
            } catch (IOException ignored) {}
        }
    }

}

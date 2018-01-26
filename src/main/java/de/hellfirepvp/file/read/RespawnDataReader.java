package de.hellfirepvp.file.read;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibConfiguration;
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
 * Class: RespawnDataReader
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:03
 */
public class RespawnDataReader {

    public static void loadData(Map<String, RespawnDataHolder.RespawnSettings> out) {
        YamlConfiguration config = LibConfiguration.getRespawnSettingsConfiguration();

        boolean changed = false;

        for(String mobName : config.getKeys(false)) {
            CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
            if(mob == null) {
                config.set(mobName, null);
                changed = true;
                continue;
            }

            ConfigurationSection section = config.getConfigurationSection(mobName);

            String locSerialized = section.getString(RESPAWN_DATA_LOCATION, "undef");
            if(locSerialized.equals("undef")) {
                config.set(mobName, null);
                changed = true;
                continue;
            }
            Location location = LocationUtils.deserializeExactLoc(locSerialized);
            long timeAfterDeath = section.getLong(RESPAWN_DATA_RESPAWNTIME, 300L);

            out.put(mobName, new RespawnDataHolder.RespawnSettings(location, timeAfterDeath));
        }

        if(changed) {
            try {
                config.save(LibConfiguration.getSpawnerDataFile());
            } catch (IOException ignored) {}
        }
    }

}

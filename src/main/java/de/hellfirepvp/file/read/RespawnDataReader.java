package de.hellfirepvp.file.read;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import de.hellfirepvp.data.mob.CustomMob;
import java.util.Iterator;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import de.hellfirepvp.util.LocationUtils;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.data.RespawnDataHolder;
import java.util.Map;

public class RespawnDataReader
{
    public static void loadData(final Map<String, RespawnDataHolder.RespawnSettings> out) {
        final YamlConfiguration config = LibConfiguration.getRespawnSettingsConfiguration();
        boolean changed = false;
        for (final String mobName : config.getKeys(false)) {
            final CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
            if (mob == null) {
                config.set(mobName, (Object)null);
                changed = true;
            }
            else {
                final ConfigurationSection section = config.getConfigurationSection(mobName);
                final String locSerialized = section.getString("loc", "undef");
                if (locSerialized.equals("undef")) {
                    config.set(mobName, (Object)null);
                    changed = true;
                }
                else {
                    final Location location = LocationUtils.deserializeExactLoc(locSerialized);
                    final long timeAfterDeath = section.getLong("time", 300L);
                    out.put(mobName, new RespawnDataHolder.RespawnSettings(location, timeAfterDeath));
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

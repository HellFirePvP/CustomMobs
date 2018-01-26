package de.hellfirepvp.file.read;

import de.hellfirepvp.util.LocationUtils;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import org.bukkit.configuration.file.YamlConfiguration;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lib.LibConfiguration;
import java.util.List;
import de.hellfirepvp.leash.LeashManager;
import java.util.UUID;
import java.util.Map;

public class LeashDataReader
{
    public static void read(final Map<UUID, LeashManager.ActiveLeashInfo> outCfg, final List<LeashManager.LeashInfo> outMobs) {
        final YamlConfiguration cfg = LibConfiguration.getLeashDataConfiguration();
        if (cfg.contains("mobsToLeash")) {
            final List<String> mobsFormatted = (List<String>)cfg.getStringList("mobsToLeash");
            for (final String s : mobsFormatted) {
                final LeashManager.LeashInfo info = deserializeInfo(s);
                if (info == null) {
                    CustomMobs.logger.info("[LeashDataReader] Could not read \"" + s + "\" - Expected format: name##-##range");
                }
                else {
                    outMobs.add(info);
                }
            }
        }
        if (cfg.contains("activeLeashes")) {
            final ConfigurationSection sec = cfg.getConfigurationSection("activeLeashes");
            for (final String s : sec.getKeys(false)) {
                try {
                    final UUID uuid = UUID.fromString(s);
                    final LeashManager.ActiveLeashInfo info2 = deserializeActive(sec.getString(s));
                    if (info2 == null) {
                        CustomMobs.logger.info("[LeashDataReader] Could not read \"" + sec.getString(s) + "\" - Expected format: world;x;y;z#range");
                    }
                    else {
                        outCfg.put(uuid, info2);
                    }
                }
                catch (Exception e) {
                    CustomMobs.logger.info("[LeashDataReader] Could not read \"" + s + "\" -> \"" + sec.getString(s) + "\"");
                }
            }
        }
    }
    
    public static String serializeActive(final LeashManager.ActiveLeashInfo info) {
        return LocationUtils.serializeExactLoc(info.to) + "#" + info.maxRange;
    }
    
    public static LeashManager.ActiveLeashInfo deserializeActive(final String in) {
        try {
            final String[] spl = in.split("#");
            return new LeashManager.ActiveLeashInfo(LocationUtils.deserializeExactLoc(spl[0]), Double.parseDouble(spl[1]));
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static String serializeInfo(final LeashManager.LeashInfo info) {
        return info.nameToLeash + "##-##" + info.maxLeashRange;
    }
    
    public static LeashManager.LeashInfo deserializeInfo(final String in) {
        try {
            final int index = in.indexOf("##-##");
            final String name = in.substring(0, index);
            return new LeashManager.LeashInfo(name, Double.parseDouble(in.substring(index + 5, in.length())));
        }
        catch (Exception e) {
            return null;
        }
    }
}

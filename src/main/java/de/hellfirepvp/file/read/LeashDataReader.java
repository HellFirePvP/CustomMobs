package de.hellfirepvp.file.read;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.util.LocationUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: LeashManager
 * Created by HellFirePvP
 * Date: 26.05.2016 / 16:43
 */
public class LeashDataReader {

    public static void read(Map<UUID, LeashManager.ActiveLeashInfo> outCfg, List<LeashManager.LeashInfo> outMobs) {
        YamlConfiguration cfg = LibConfiguration.getLeashDataConfiguration();

        if(cfg.contains("mobsToLeash")) {
            List<String> mobsFormatted = cfg.getStringList("mobsToLeash");
            for (String s : mobsFormatted) {
                LeashManager.LeashInfo info = deserializeInfo(s);
                if(info == null) {
                    CustomMobs.logger.info("[LeashDataReader] Could not read \"" + s + "\" - Expected format: name##-##range");
                } else {
                    outMobs.add(info);
                }
            }
        }

        if(cfg.contains("activeLeashes")) {
            ConfigurationSection sec = cfg.getConfigurationSection("activeLeashes");
            for (String s : sec.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(s);
                    LeashManager.ActiveLeashInfo info = deserializeActive(sec.getString(s));
                    if(info == null) {
                        CustomMobs.logger.info("[LeashDataReader] Could not read \"" + sec.getString(s) + "\" - Expected format: world;x;y;z#range");
                    } else {
                        outCfg.put(uuid, info);
                    }
                } catch (Exception e) {
                    CustomMobs.logger.info("[LeashDataReader] Could not read \"" + s + "\" -> \"" + sec.getString(s) + "\"");
                }

            }
        }
    }

    public static String serializeActive(LeashManager.ActiveLeashInfo info) {
        return LocationUtils.serializeExactLoc(info.to) + "#" + info.maxRange;
    }

    public static LeashManager.ActiveLeashInfo deserializeActive(String in) {
        try {
            String[] spl = in.split("#");
            return new LeashManager.ActiveLeashInfo(LocationUtils.deserializeExactLoc(spl[0]), Double.parseDouble(spl[1]));
        } catch (Exception e) {
            return null;
        }
    }

    public static String serializeInfo(LeashManager.LeashInfo info) {
        return info.nameToLeash + "##-##" + info.maxLeashRange;
    }

    public static LeashManager.LeashInfo deserializeInfo(String in) {
        try {
            int index = in.indexOf("##-##");
            String name = in.substring(0, index);
            return new LeashManager.LeashInfo(name, Double.parseDouble(in.substring(index + 5, in.length())));
        } catch (Exception e) {
            return null;
        }
    }

}

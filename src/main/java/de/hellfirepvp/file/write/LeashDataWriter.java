package de.hellfirepvp.file.write;

import de.hellfirepvp.file.read.LeashDataReader;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.lib.LibConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * HellFirePvP@Admin
 * Date: 14.03.2016 / 09:41
 * on Project CustomMobs
 * LeashDataWriter
 */
public class LeashDataWriter {

    public static void writeToFile(Map<UUID, LeashManager.ActiveLeashInfo> leashMap, List<LeashManager.LeashInfo> mobsToLeash) {
        YamlConfiguration cfg = LibConfiguration.getLeashDataConfiguration();

        List<String> mobs = new ArrayList<>();
        for (LeashManager.LeashInfo info : mobsToLeash) {
            mobs.add(LeashDataReader.serializeInfo(info));
        }
        cfg.set("mobsToLeash", mobs);

        ConfigurationSection sec = cfg.createSection("activeLeashes");
        for (Map.Entry<UUID, LeashManager.ActiveLeashInfo> entry : leashMap.entrySet()) {
            sec.set(entry.getKey().toString(), LeashDataReader.serializeActive(entry.getValue()));
        }

        try {
            cfg.save(LibConfiguration.getLeashDataFile());
        } catch (IOException e) {}
    }

}

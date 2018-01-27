package de.hellfirepvp.file.write;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import de.hellfirepvp.file.read.LeashDataReader;
import java.util.ArrayList;
import de.hellfirepvp.lib.LibConfiguration;
import java.util.List;
import de.hellfirepvp.leash.LeashManager;
import java.util.UUID;
import java.util.Map;

public class LeashDataWriter
{
    public static void writeToFile(final Map<UUID, LeashManager.ActiveLeashInfo> leashMap, final List<LeashManager.LeashInfo> mobsToLeash) {
        final YamlConfiguration cfg = LibConfiguration.getLeashDataConfiguration();
        final List<String> mobs = new ArrayList<String>();
        for (final LeashManager.LeashInfo info : mobsToLeash) {
            mobs.add(LeashDataReader.serializeInfo(info));
        }
        cfg.set("mobsToLeash", (Object)mobs);
        final ConfigurationSection sec = cfg.createSection("activeLeashes");
        for (final Map.Entry<UUID, LeashManager.ActiveLeashInfo> entry : leashMap.entrySet()) {
            sec.set(entry.getKey().toString(), (Object)LeashDataReader.serializeActive(entry.getValue()));
        }
        try {
            cfg.save(LibConfiguration.getLeashDataFile());
        }
        catch (IOException ex) {}
    }
}

package de.hellfirepvp.file.write;

import de.hellfirepvp.data.StackingDataHolder;
import de.hellfirepvp.lib.LibConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: StackingDataWriter
 * Created by HellFirePvP
 * Date: 31.05.2016 / 19:45
 */
public class StackingDataWriter {

    public static void write(List<StackingDataHolder.MobStack> stacks) {
        YamlConfiguration out = new YamlConfiguration();
        for (StackingDataHolder.MobStack stack : stacks) {
            out.set(stack.mounted, stack.mounting);
        }
        try {
            out.save(LibConfiguration.getStackSettingsFile());
        } catch (IOException ignored) {}
    }

}

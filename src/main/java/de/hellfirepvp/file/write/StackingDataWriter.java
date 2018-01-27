package de.hellfirepvp.file.write;

import java.util.Iterator;
import java.io.IOException;
import de.hellfirepvp.lib.LibConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import de.hellfirepvp.data.StackingDataHolder;
import java.util.List;

public class StackingDataWriter
{
    public static void write(final List<StackingDataHolder.MobStack> stacks) {
        final YamlConfiguration out = new YamlConfiguration();
        for (final StackingDataHolder.MobStack stack : stacks) {
            out.set(stack.mounted, (Object)stack.mounting);
        }
        try {
            out.save(LibConfiguration.getStackSettingsFile());
        }
        catch (IOException ex) {}
    }
}

package de.hellfirepvp.file.read;

import java.util.Iterator;
import org.bukkit.configuration.file.YamlConfiguration;
import de.hellfirepvp.CustomMobs;
import java.util.LinkedList;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.data.StackingDataHolder;
import java.util.List;

public class StackingDataReader
{
    public static List<StackingDataHolder.MobStack> readStackingData() {
        final YamlConfiguration cfg = LibConfiguration.getStackDataConfiguration();
        final List<StackingDataHolder.MobStack> stacking = new LinkedList<StackingDataHolder.MobStack>();
        for (final String mounted : cfg.getKeys(false)) {
            if (CustomMobs.instance.getMobDataHolder().getCustomMob(mounted) == null) {
                CustomMobs.logger.info("Skipping StackEntry of " + mounted + " because the mob doesn't exist anymore apparently...");
            }
            else {
                final String mounting = cfg.getString(mounted);
                if (CustomMobs.instance.getMobDataHolder().getCustomMob(mounting) == null) {
                    CustomMobs.logger.info("Skipping StackEntry of " + mounting + " because the mob doesn't exist anymore apparently...");
                }
                else {
                    final StackingDataHolder.MobStack newStack = new StackingDataHolder.MobStack(mounted, mounting);
                    if (!StackingDataHolder.discoverCircularStacking(stacking, newStack)) {
                        stacking.add(newStack);
                    }
                    else {
                        CustomMobs.logger.warning("Discovered circular MobStack if " + mounting + " would mount " + mounted);
                        CustomMobs.logger.warning("Skipping that entry...");
                    }
                }
            }
        }
        return stacking;
    }
}

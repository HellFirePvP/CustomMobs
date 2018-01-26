package de.hellfirepvp.file.read;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.StackingDataHolder;
import de.hellfirepvp.lib.LibConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: StackingDataReader
 * Created by HellFirePvP
 * Date: 31.05.2016 / 19:13
 */
public class StackingDataReader {

    public static List<StackingDataHolder.MobStack> readStackingData() {
        YamlConfiguration cfg = LibConfiguration.getStackDataConfiguration();
        List<StackingDataHolder.MobStack> stacking = new LinkedList<>();

        for (String mounted : cfg.getKeys(false)) {
            if(CustomMobs.instance.getMobDataHolder().getCustomMob(mounted) == null) {
                CustomMobs.logger.info("Skipping StackEntry of " + mounted + " because the mob doesn't exist anymore apparently...");
                continue;
            }
            String mounting = cfg.getString(mounted);
            if(CustomMobs.instance.getMobDataHolder().getCustomMob(mounting) == null) {
                CustomMobs.logger.info("Skipping StackEntry of " + mounting + " because the mob doesn't exist anymore apparently...");
                continue;
            }
            StackingDataHolder.MobStack newStack = new StackingDataHolder.MobStack(mounted, mounting);
            if(!StackingDataHolder.discoverCircularStacking(stacking, newStack)) {
                stacking.add(newStack);
            } else {
                CustomMobs.logger.warning("Discovered circular MobStack if " + mounting + " would mount " + mounted);
                CustomMobs.logger.warning("Skipping that entry...");
            }
        }
        return stacking;
    }

}

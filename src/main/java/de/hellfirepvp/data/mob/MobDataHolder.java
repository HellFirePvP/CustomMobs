package de.hellfirepvp.data.mob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.file.read.MobDataReader;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: MobDataHolder
 * Created by HellFirePvP
 * Date: 24.05.2016 / 16:37
 */
public class MobDataHolder {

    private Map<String, CustomMob> loadedMobs = new HashMap<>();

    public void reloadAllMobs() {
        CustomMobs.logger.debug("Reloading all Mobfiles!");

        loadedMobs.clear();
        for (File mobFile : CustomMobs.instance.getMobDataFolder().listFiles()) {
            try {
                String name = mobFile.getName().replace(".dat", "");
                CustomMob mob = MobDataReader.loadCustomMob(mobFile, name);
                if(mob == null) {
                    CustomMobs.logger.warning("Skipping loading of " + mobFile.getName());
                    continue;
                }

                loadedMobs.put(mob.getMobFileName(), mob);
            } catch (Exception exc) {
                exc.printStackTrace();
                CustomMobs.logger.severe("Skipping loading of " + mobFile.getName() + "! Reason: Corrupted File!");
            }
        }

        CustomMobs.logger.debug("Finished reloading all Mobfiles!");
    }

    public CustomMob getCustomMob(String name) {
        return loadedMobs.get(name);
    }

    public Collection<CustomMob> getAllLoadedMobs() {
        return Collections.unmodifiableCollection(loadedMobs.values());
    }

}

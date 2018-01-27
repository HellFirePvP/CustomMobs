package de.hellfirepvp.data.mob;

import java.util.Collections;
import java.util.Collection;
import java.io.File;
import de.hellfirepvp.file.read.MobDataReader;
import de.hellfirepvp.CustomMobs;
import java.util.HashMap;
import java.util.Map;

public class MobDataHolder
{
    private Map<String, CustomMob> loadedMobs;
    
    public MobDataHolder() {
        this.loadedMobs = new HashMap<String, CustomMob>();
    }
    
    public void reloadAllMobs() {
        CustomMobs.logger.debug("Reloading all Mobfiles!");
        this.loadedMobs.clear();
        for (final File mobFile : CustomMobs.instance.getMobDataFolder().listFiles()) {
            try {
                final String name = mobFile.getName().replace(".dat", "");
                final CustomMob mob = MobDataReader.loadCustomMob(mobFile, name);
                if (mob == null) {
                    CustomMobs.logger.warning("Skipping loading of " + mobFile.getName());
                }
                else {
                    this.loadedMobs.put(mob.getMobFileName(), mob);
                }
            }
            catch (Exception exc) {
                exc.printStackTrace();
                CustomMobs.logger.severe("Skipping loading of " + mobFile.getName() + "! Reason: Corrupted File!");
            }
        }
        CustomMobs.logger.debug("Finished reloading all Mobfiles!");
    }
    
    public CustomMob getCustomMob(final String name) {
        return this.loadedMobs.get(name);
    }
    
    public Collection<CustomMob> getAllLoadedMobs() {
        return Collections.unmodifiableCollection((Collection<? extends CustomMob>)this.loadedMobs.values());
    }
}

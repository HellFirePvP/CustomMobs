package de.hellfirepvp.file.write;

import java.io.OutputStream;
import de.hellfirepvp.data.nbt.base.NBTWrapper;
import java.io.FileOutputStream;
import java.io.IOException;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.CustomMobs;
import java.io.File;
import de.hellfirepvp.data.mob.CustomMob;

public class MobDataWriter
{
    public static File getMobFile(final CustomMob mob) {
        return new File(CustomMobs.instance.getMobDataFolder(), mob.getMobFileName() + ".dat");
    }
    
    public static File getMobFile(final String mobFileName) {
        return new File(CustomMobs.instance.getMobDataFolder(), mobFileName + ".dat");
    }
    
    public static void writeMobFile(final CustomMob mob) {
        writeMobFile(mob.getDataSnapshot(), getMobFile(mob));
    }
    
    public static void writeMobFile(final WrappedNBTTagCompound tag, final CustomMob customMob) {
        writeMobFile(tag, getMobFile(customMob));
    }
    
    public static void writeMobFile(final WrappedNBTTagCompound tag, final File mobFile) {
        if (!mobFile.exists()) {
            try {
                mobFile.createNewFile();
            }
            catch (IOException e) {
                CustomMobs.logger.warning("Could not create mobfile: " + mobFile.getName());
                return;
            }
        }
        try (final FileOutputStream fos = new FileOutputStream(mobFile)) {
            NBTWrapper.writeTagToOutputStream(tag, fos);
        }
        catch (IOException exc) {
            CustomMobs.logger.warning("Writing into mobfile failed: " + mobFile.getName());
            exc.printStackTrace();
        }
    }
}

package de.hellfirepvp.file.write;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.nbt.base.NBTWrapper;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: MobDataWriter
 * Created by HellFirePvP
 * Date: 24.05.2016 / 16:58
 */
public class MobDataWriter {

    public static File getMobFile(CustomMob mob) {
        return new File(CustomMobs.instance.getMobDataFolder(), mob.getMobFileName() + ".dat");
    }

    public static File getMobFile(String mobFileName) {
        return new File(CustomMobs.instance.getMobDataFolder(), mobFileName + ".dat");
    }

    public static void writeMobFile(CustomMob mob) {
        writeMobFile(mob.getDataSnapshot(), getMobFile(mob));
    }

    public static void writeMobFile(WrappedNBTTagCompound tag, CustomMob customMob) {
        writeMobFile(tag, getMobFile(customMob));
    }

    public static void writeMobFile(WrappedNBTTagCompound tag, File mobFile) {
        if(!mobFile.exists()) {
            try {
                mobFile.createNewFile();
            } catch (IOException e) {
                CustomMobs.logger.warning("Could not create mobfile: " + mobFile.getName());
                return;
            }
        }

        try (FileOutputStream fos = new FileOutputStream(mobFile)) {
            NBTWrapper.writeTagToOutputStream(tag, fos);
        } catch (IOException exc) {
            CustomMobs.logger.warning("Writing into mobfile failed: " + mobFile.getName());
            exc.printStackTrace();
        }
    }

}

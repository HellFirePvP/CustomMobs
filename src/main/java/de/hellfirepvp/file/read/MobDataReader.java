package de.hellfirepvp.file.read;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.nbt.base.NBTWrapper;
import de.hellfirepvp.data.nbt.base.WrappedNBTTagCompound;
import de.hellfirepvp.nms.NMSReflector;

import java.io.File;
import java.io.FileInputStream;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: MobDataReader
 * Created by HellFirePvP
 * Date: 24.05.2016 / 16:39
 */
public class MobDataReader {

    public static CustomMob loadCustomMob(File mobFile, String name) {
        WrappedNBTTagCompound dataTag;
        try (FileInputStream fis = new FileInputStream(mobFile)) {
            dataTag = NBTWrapper.readFromInputStream(fis);
        } catch (Exception e) {
            CustomMobs.logger.info("Can't read NBT tag from file " + mobFile.getName() + " - Skipping loading...");
            return null;
        }

        //We just make sure that the type exists and we don't run into minecrafts 1000-line error messages later hundreds of times.
        String type = (String) dataTag.getValue("id");
        if(type == null) {
            CustomMobs.logger.info("Can't find Type-Tag for " + mobFile.getName() + " - Skipping loading...");
            return null;
        }
        if(!NMSReflector.mobTypeProvider.getTypeNames().contains(type)) {
            CustomMobs.logger.info("Unknown mobType (" + type + ") found in file " + mobFile.getName() + " - Skipping loading...");
            return null;
        }

        dataTag.setInt("Spigot.ticksLived", 0);
        if(dataTag.hasKey("Leashed")) {
            dataTag.setBoolean("Leashed", false);
        }
        return new CustomMob(name, dataTag);
    }

}

package de.hellfirepvp.file.read;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.nms.RegistryTypeProvider;
import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.CustomMobs;
import java.io.InputStream;
import de.hellfirepvp.data.nbt.base.NBTWrapper;
import java.io.FileInputStream;
import de.hellfirepvp.data.mob.CustomMob;
import java.io.File;

public class MobDataReader
{
    public static CustomMob loadCustomMob(final File mobFile, final String name) {
        WrappedNBTTagCompound dataTag;
        try (final FileInputStream fis = new FileInputStream(mobFile)) {
            dataTag = NBTWrapper.readFromInputStream(fis);
        }
        catch (Exception e) {
            CustomMobs.logger.info("Can't read NBT tag from file " + mobFile.getName() + " - Skipping loading...");
            return null;
        }
        String type = (String)dataTag.getValue("id");
        if (type == null) {
            CustomMobs.logger.info("Can't find Type-Tag for " + mobFile.getName() + " - Skipping loading...");
            return null;
        }
        if (CustomMobs.currentVersion == SupportedVersions.V1_12_R1) {
            if (!((RegistryTypeProvider)NMSReflector.mobTypeProvider).doesMobTypeExist(type)) {
                final String newType = ((RegistryTypeProvider)NMSReflector.mobTypeProvider).tryTranslateNameToRegistry(type);
                if (newType != null) {
                    dataTag.setString("id", newType);
                    CustomMobs.logger.info("Changed outdated mobTypeName '" + type + "' to '" + newType + "' !");
                }
                type = newType;
            }
            if (type == null || !((RegistryTypeProvider)NMSReflector.mobTypeProvider).doesMobTypeExist(type)) {
                CustomMobs.logger.info("Unknown mobType (" + type + ") found in file " + mobFile.getName() + " - Skipping loading...");
                return null;
            }
        }
        else if (!NMSReflector.mobTypeProvider.getTypeNames().contains(type)) {
            CustomMobs.logger.info("Unknown mobType (" + type + ") found in file " + mobFile.getName() + " - Skipping loading...");
            return null;
        }
        try {
            convertData(dataTag);
        }
        catch (Exception exc) {
            CustomMobs.logger.debug("Data conversion failed for " + mobFile.getName());
        }
        dataTag.setInt("Spigot.ticksLived", 0);
        if (dataTag.hasKey("Leashed")) {
            dataTag.setBoolean("Leashed", false);
        }
        if (dataTag.hasKey("CustomMobs")) {
            final WrappedNBTTagCompound cmp = dataTag.getTagCompound("CustomMobs");
            if (cmp != null && cmp.hasKey("Experience")) {
                final int exp = (int)cmp.getValue("Experience");
                cmp.removeKey("Experience");
                cmp.setInt("ExperienceLower", exp);
                cmp.setInt("ExperienceHigher", exp);
                dataTag.setSubTag("CustomMobs", cmp);
            }
        }
        return new CustomMob(name, dataTag);
    }
    
    private static void convertData(final WrappedNBTTagCompound dataTag) {
        if (CustomMobs.currentVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_12_R1)) {
            final String type = (String)dataTag.getValue("id");
            if (type.equalsIgnoreCase("minecraft:skeleton")) {
                if (dataTag.hasKey("SkeletonType")) {
                    final int id = (int)dataTag.getValue("SkeletonType");
                    if (id == 1) {
                        dataTag.setString("id", "minecraft:wither_skeleton");
                    }
                    else if (id == 2) {
                        dataTag.setString("id", "minecraft:stray");
                    }
                }
            }
            else if (type.equalsIgnoreCase("minecraft:guardian")) {
                if (dataTag.hasKey("Elder") && (boolean)dataTag.getValue("Elder")) {
                    dataTag.setString("id", "minecraft:elder_guardian");
                }
            }
            else if (type.equalsIgnoreCase("minecraft:zombie") && dataTag.hasKey("ZombieType")) {
                final int id = (int)dataTag.getValue("ZombieType");
                if (id == 6) {
                    dataTag.setString("id", "minecraft:husk");
                }
                else if (id != 0) {
                    dataTag.setString("id", "minecraft:zombie_villager");
                }
            }
        }
    }
}

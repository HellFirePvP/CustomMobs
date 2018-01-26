package de.hellfirepvp.data.mob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: DataAdapter
 * Created by HellFirePvP
 * Date: 26.05.2016 / 15:49
 */
public class DataAdapter {

    private CustomMob parentMob;

    public DataAdapter(CustomMob parentMob) {
        this.parentMob = parentMob;
    }

    public WrappedNBTTagCompound getPersistentCustomMobsTag() {
        if(!parentMob.snapshotTag.hasKey("CustomMobs")) {
            parentMob.snapshotTag.setSubTag("CustomMobs", NMSReflector.nbtProvider.newTagCompound());
        }

        return parentMob.snapshotTag.getTagCompound("CustomMobs");
    }

    public boolean saveTagWithPair(String entry, Object value) {
        return parentMob.saveTagAlongWith(entry, value);
    }

    public void setCommandToExecute(String command) {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(command == null) {
            cmobTag.removeKey("Command");
        } else {
            cmobTag.setString("Command", command);
        }
        parentMob.updateTag();
    }

    public void setSpawnLimit(int limit) {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(limit <= 0) {
            cmobTag.removeKey("SpawnLimit");
        } else {
            cmobTag.setInt("SpawnLimit", limit);
        }
        parentMob.updateTag();
        CustomMobs.instance.getSpawnLimiter().reloadSingleMob(parentMob.getMobFileName(), limit);
    }

    public void setExperienceDropRange(int lowerEDrop, int higherExpDrop) {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        cmobTag.setInt("ExperienceLower", lowerEDrop);
        cmobTag.setInt("ExperienceHigher", higherExpDrop);
        parentMob.updateTag();
    }

    public void setFireProof(boolean fireProof) {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        cmobTag.setBoolean("FireProof", fireProof);
        parentMob.updateTag();
    }

    public void setDrops(List<ICustomMob.ItemDrop> drops) {
        WrappedNBTTagList tagList = NMSReflector.nbtProvider.newTagList();
        for (ICustomMob.ItemDrop drop : drops) {
            Double chance = drop.getChance();
            WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
            NMSReflector.nbtProvider.saveStack(drop.getStack(), tag);
            WrappedNBTTagCompound dropTag = NMSReflector.nbtProvider.newTagCompound();
            dropTag.setSubTag("ItemStack", tag);
            dropTag.setDouble("Chance", chance);
            tagList.appendTagCompound(dropTag);
        }

        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        cmobTag.setSubList("Drops", tagList);
        parentMob.updateTag();
    }

    public String getCommandToExecute() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(cmobTag.hasKey("Command")) {
            return (String) cmobTag.getValue("Command");
        } else {
            return null;
        }
    }

    public int getSpawnLimit() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(cmobTag.hasKey("SpawnLimit")) {
            return (int) cmobTag.getValue("SpawnLimit");
        } else {
            return -1;
        }
    }

    public int getExpDropLower() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(cmobTag.hasKey("ExperienceLower")) {
            return (int) cmobTag.getValue("ExperienceLower");
        } else {
            return 0;
        }
    }

    public int getExpDropHigher() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(cmobTag.hasKey("ExperienceHigher")) {
            return (int) cmobTag.getValue("ExperienceHigher");
        } else {
            return 0;
        }
    }

    public boolean isFireProof() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        return cmobTag.hasKey("FireProof") && ((byte) cmobTag.getValue("FireProof") != 0);
    }

    public List<ICustomMob.ItemDrop> getItemDrops() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(!cmobTag.hasKey("Drops")) return new LinkedList<>();
        WrappedNBTTagList dropList = cmobTag.getTagList("Drops", NBTTagType.TAG_COMPOUND);
        List<ICustomMob.ItemDrop> drops = new LinkedList<>();
        Iterator listIt = dropList.getElementIterator();
        while (listIt.hasNext()) {
            Object tag = listIt.next();
            if(tag != null && tag instanceof WrappedNBTTagCompound) {
                WrappedNBTTagCompound dropTag = (WrappedNBTTagCompound) tag;
                double chance = (double) dropTag.getValue("Chance");
                ItemStack stack = NMSReflector.nbtProvider.loadStack(dropTag.getTagCompound("ItemStack"));
                if(stack != null) {
                    drops.add(new ICustomMob.ItemDrop(stack, chance));
                }
            }
        }
        return drops;
    }
}

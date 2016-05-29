package de.hellfirepvp.data.mob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.nbt.base.NBTTagType;
import de.hellfirepvp.data.nbt.base.WrappedNBTTagCompound;
import de.hellfirepvp.data.nbt.base.WrappedNBTTagList;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
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
        CustomMobs.instance.getSpawnLimiter().reloadMob(parentMob);
    }

    public void setExperienceDrop(int experienceDrop) {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        cmobTag.setInt("Experience", experienceDrop);
        parentMob.updateTag();
    }

    public void setFireProof(boolean fireProof) {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        cmobTag.setBoolean("FireProof", fireProof);
        parentMob.updateTag();
    }

    public void setDrops(Map<ItemStack, Double> drops) {
        WrappedNBTTagList tagList = NMSReflector.nbtProvider.newTagList();
        for (ItemStack item : drops.keySet()) {
            Double chance = drops.get(item);
            WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
            NMSReflector.nbtProvider.saveStack(item, tag);
            WrappedNBTTagCompound dropTag = NMSReflector.nbtProvider.newTagCompound();
            dropTag.setSubTag("ItemStack", tag);
            dropTag.setDouble("Chance", chance);

            if(tagList.appendTagCompound(dropTag)) {
                System.out.println("Successfully added dropTag");
            } else {
                System.out.println("Failed to add drop tag");
            }
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

    public int getExperienceDrop() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(cmobTag.hasKey("Experience")) {
            return (int) cmobTag.getValue("Experience");
        } else {
            return 0;
        }
    }

    public boolean isFireProof() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        return cmobTag.hasKey("FireProof") && (boolean) cmobTag.getValue("FireProof");
    }

    public Map<ItemStack, Double> getItemDrops() {
        WrappedNBTTagCompound cmobTag = getPersistentCustomMobsTag();
        if(!cmobTag.hasKey("Drops")) return new HashMap<>();
        WrappedNBTTagList dropList = cmobTag.getTagList("Drops", NBTTagType.TAG_COMPOUND);
        Map<ItemStack, Double> dropMap = new HashMap<>();
        Iterator listIt = dropList.getImmutableElementIterator();
        while (listIt.hasNext()) {
            Object tag = listIt.next();
            if(tag != null && tag instanceof WrappedNBTTagCompound) {
                WrappedNBTTagCompound dropTag = (WrappedNBTTagCompound) tag;
                double chance = (double) dropTag.getValue("Chance");
                ItemStack stack = NMSReflector.nbtProvider.loadStack(dropTag.getTagCompound("ItemStack"));
                if(stack != null) {
                    dropMap.put(stack, chance);
                }
            }
        }
        return dropMap;
    }
}

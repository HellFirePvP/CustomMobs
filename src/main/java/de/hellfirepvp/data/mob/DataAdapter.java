package de.hellfirepvp.data.mob;

import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import java.util.LinkedList;
import java.util.Iterator;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.api.data.ICustomMob;
import java.util.List;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;

public class DataAdapter
{
    private CustomMob parentMob;
    
    public DataAdapter(final CustomMob parentMob) {
        this.parentMob = parentMob;
    }
    
    public WrappedNBTTagCompound getPersistentCustomMobsTag() {
        if (!this.parentMob.snapshotTag.hasKey("CustomMobs")) {
            this.parentMob.snapshotTag.setSubTag("CustomMobs", NMSReflector.nbtProvider.newTagCompound());
        }
        return this.parentMob.snapshotTag.getTagCompound("CustomMobs");
    }
    
    public boolean saveTagWithPair(final String entry, final Object value) {
        return this.parentMob.saveTagAlongWith(entry, value);
    }
    
    public void setCommandToExecute(final String command) {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        if (command == null) {
            cmobTag.removeKey("Command");
        }
        else {
            cmobTag.setString("Command", command);
        }
        this.parentMob.updateTag();
    }
    
    public void setSpawnLimit(final int limit) {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        if (limit <= 0) {
            cmobTag.removeKey("SpawnLimit");
        }
        else {
            cmobTag.setInt("SpawnLimit", limit);
        }
        this.parentMob.updateTag();
        CustomMobs.instance.getSpawnLimiter().reloadSingleMob(this.parentMob.getMobFileName(), limit);
    }
    
    public void setExperienceDropRange(final int lowerEDrop, final int higherExpDrop) {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        cmobTag.setInt("ExperienceLower", lowerEDrop);
        cmobTag.setInt("ExperienceHigher", higherExpDrop);
        this.parentMob.updateTag();
    }
    
    public void setFireProof(final boolean fireProof) {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        cmobTag.setBoolean("FireProof", fireProof);
        this.parentMob.updateTag();
    }
    
    public void setDrops(final List<ICustomMob.ItemDrop> drops) {
        final WrappedNBTTagList tagList = NMSReflector.nbtProvider.newTagList();
        for (final ICustomMob.ItemDrop drop : drops) {
            final Double chance = drop.getChance();
            final WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
            NMSReflector.nbtProvider.saveStack(drop.getStack(), tag);
            final WrappedNBTTagCompound dropTag = NMSReflector.nbtProvider.newTagCompound();
            dropTag.setSubTag("ItemStack", tag);
            dropTag.setDouble("Chance", chance);
            tagList.appendTagCompound(dropTag);
        }
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        cmobTag.setSubList("Drops", tagList);
        this.parentMob.updateTag();
    }
    
    public String getCommandToExecute() {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        if (cmobTag.hasKey("Command")) {
            return (String)cmobTag.getValue("Command");
        }
        return null;
    }
    
    public int getSpawnLimit() {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        if (cmobTag.hasKey("SpawnLimit")) {
            return (int)cmobTag.getValue("SpawnLimit");
        }
        return -1;
    }
    
    public int getExpDropLower() {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        if (cmobTag.hasKey("ExperienceLower")) {
            return (int)cmobTag.getValue("ExperienceLower");
        }
        return 0;
    }
    
    public int getExpDropHigher() {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        if (cmobTag.hasKey("ExperienceHigher")) {
            return (int)cmobTag.getValue("ExperienceHigher");
        }
        return 0;
    }
    
    public boolean isFireProof() {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        return cmobTag.hasKey("FireProof") && (byte)cmobTag.getValue("FireProof") != 0;
    }
    
    public List<ICustomMob.ItemDrop> getItemDrops() {
        final WrappedNBTTagCompound cmobTag = this.getPersistentCustomMobsTag();
        if (!cmobTag.hasKey("Drops")) {
            return new LinkedList<ICustomMob.ItemDrop>();
        }
        final WrappedNBTTagList dropList = cmobTag.getTagList("Drops", NBTTagType.TAG_COMPOUND);
        final List<ICustomMob.ItemDrop> drops = new LinkedList<ICustomMob.ItemDrop>();
        final Iterator listIt = dropList.getElementIterator();
        while (listIt.hasNext()) {
            final Object tag = listIt.next();
            if (tag != null && tag instanceof WrappedNBTTagCompound) {
                final WrappedNBTTagCompound dropTag = (WrappedNBTTagCompound)tag;
                final double chance = (double)dropTag.getValue("Chance");
                final ItemStack stack = NMSReflector.nbtProvider.loadStack(dropTag.getTagCompound("ItemStack"));
                if (stack == null) {
                    continue;
                }
                drops.add(new ICustomMob.ItemDrop(stack, chance));
            }
        }
        return drops;
    }
}

package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.NBTTagType;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import javax.annotation.Nullable;

public interface WatchedNBTEditor
{
    void saveAndInvalidateTag();
    
    ICustomMob getOwner();
    
    void removeKey(final String p0);
    
    boolean hasKey(final String p0);
    
    @Nullable
    Object getValue(final String p0);
    
    void setInt(final String p0, final int p1);
    
    void setByte(final String p0, final byte p1);
    
    void setShort(final String p0, final short p1);
    
    void setLong(final String p0, final long p1);
    
    void setFloat(final String p0, final float p1);
    
    void setDouble(final String p0, final double p1);
    
    void setBoolean(final String p0, final boolean p1);
    
    void setString(final String p0, final String p1);
    
    void setIntArray(final String p0, final int[] p1);
    
    void setByteArray(final String p0, final byte[] p1);
    
    void setSubTag(final String p0, final WrappedNBTTagCompound p1);
    
    void setSubList(final String p0, final WrappedNBTTagList p1);
    
    void setItemStack(final String p0, final ItemStack p1);
    
    APIWrappedNBTTagCompound createOrGetSubTag(final String p0);
    
    APIWrappedNBTTagList createOrGetSubList(final String p0, final NBTTagType p1);
    
    @Nullable
    APIWrappedNBTTagCompound getTagCompound(final String p0);
    
    @Nullable
    APIWrappedNBTTagList getTagList(final String p0, final NBTTagType p1);
}

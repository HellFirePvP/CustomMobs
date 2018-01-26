package de.hellfirepvp.api.data.nbt;

import javax.annotation.Nullable;
import org.bukkit.inventory.ItemStack;

public interface WrappedNBTTagCompound
{
    Object getRawNMSTagCompound();
    
    void removeKey(final String p0);
    
    boolean hasKey(final String p0);
    
    void set(final String p0, final Object p1) throws UnsupportedNBTTypeException;
    
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
    
    @Nullable
    ItemStack getItemStack(final String p0);
    
    void setItemStack(final String p0, final ItemStack p1);
    
    @Nullable
    WrappedNBTTagCompound getTagCompound(final String p0);
    
    @Nullable
    WrappedNBTTagList getTagList(final String p0, final NBTTagType p1);
    
    @Nullable
    Object getValue(final String p0);
    
    WrappedNBTTagCompound unmodifiable();
}

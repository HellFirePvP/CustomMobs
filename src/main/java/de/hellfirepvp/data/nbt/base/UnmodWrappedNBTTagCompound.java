package de.hellfirepvp.data.nbt.base;

import de.hellfirepvp.api.data.nbt.NBTTagType;
import javax.annotation.Nullable;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.api.data.nbt.UnsupportedNBTTypeException;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;

public class UnmodWrappedNBTTagCompound implements WrappedNBTTagCompound
{
    private final WrappedNBTTagCompound parent;
    
    public UnmodWrappedNBTTagCompound(final WrappedNBTTagCompound parent) {
        this.parent = parent;
    }
    
    @Override
    public Object getRawNMSTagCompound() {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void removeKey(final String key) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public boolean hasKey(final String key) {
        return this.parent.hasKey(key);
    }
    
    @Override
    public void set(final String key, final Object value) throws UnsupportedNBTTypeException {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setInt(final String key, final int value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setByte(final String key, final byte value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setShort(final String key, final short value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setLong(final String key, final long value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setFloat(final String key, final float value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setDouble(final String key, final double value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setBoolean(final String key, final boolean value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setString(final String key, final String value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setIntArray(final String key, final int[] value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setByteArray(final String key, final byte[] value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setSubTag(final String key, final WrappedNBTTagCompound subTag) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public void setSubList(final String key, final WrappedNBTTagList subList) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Nullable
    @Override
    public ItemStack getItemStack(final String key) {
        return this.parent.getItemStack(key);
    }
    
    @Override
    public void setItemStack(final String key, final ItemStack stack) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public WrappedNBTTagCompound getTagCompound(final String key) {
        final WrappedNBTTagCompound tag = this.parent.getTagCompound(key);
        if (tag != null) {
            return new UnmodWrappedNBTTagCompound(tag);
        }
        return null;
    }
    
    @Override
    public WrappedNBTTagList getTagList(final String key, final NBTTagType expectedListElements) {
        final WrappedNBTTagList list = this.parent.getTagList(key, expectedListElements);
        if (list != null) {
            return new UnmodWrappedNBTTagList(list);
        }
        return null;
    }
    
    @Override
    public Object getValue(final String key) {
        return this.parent.getValue(key);
    }
    
    @Override
    public WrappedNBTTagCompound unmodifiable() {
        return this;
    }
}

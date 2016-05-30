package de.hellfirepvp.data.nbt.base;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: UnmodWrappedNBTTagCompound
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:15
 */
public class UnmodWrappedNBTTagCompound implements WrappedNBTTagCompound {

    private final WrappedNBTTagCompound parent;

    public UnmodWrappedNBTTagCompound(WrappedNBTTagCompound parent) {
        this.parent = parent;
    }

    @Override
    public Object getRawNMSTagCompound() {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void removeKey(String key) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public boolean hasKey(String key) {
        return parent.hasKey(key);
    }

    @Override
    public void set(String key, Object value) throws UnsupportedNBTTypeException {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setInt(String key, int value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setByte(String key, byte value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setShort(String key, short value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setLong(String key, long value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setFloat(String key, float value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setDouble(String key, double value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setBoolean(String key, boolean value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setString(String key, String value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setIntArray(String key, int[] value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setByteArray(String key, byte[] value) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setSubTag(String key, WrappedNBTTagCompound subTag) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public void setSubList(String key, WrappedNBTTagList subList) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public WrappedNBTTagCompound getTagCompound(String key) {
        WrappedNBTTagCompound tag = parent.getTagCompound(key);
        if(tag != null) {
            return new UnmodWrappedNBTTagCompound(tag);
        }
        return null;
    }

    @Override
    public WrappedNBTTagList getTagList(String key, NBTTagType expectedListElements) {
        WrappedNBTTagList list = parent.getTagList(key, expectedListElements);
        if(list != null) {
            return new UnmodWrappedNBTTagList(list);
        }
        return null;
    }

    @Override
    public Object getValue(String key) {
        return parent.getValue(key);
    }

    @Override
    public WrappedNBTTagCompound unmodifiable() {
        return this;
    }
}

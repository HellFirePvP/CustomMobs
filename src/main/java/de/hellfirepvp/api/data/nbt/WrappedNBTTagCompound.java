package de.hellfirepvp.api.data.nbt;

import de.hellfirepvp.data.nbt.base.NBTTagType;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WrappedNBTTag
 * Created by HellFirePvP
 * Date: 24.05.2016 / 00:08
 */
public interface WrappedNBTTagCompound {

    public Object getRawNMSTagCompound();

    public void removeKey(String key);

    public boolean hasKey(String key);

    public void set(String key, Object value) throws UnsupportedNBTTypeException;

    public void setInt(String key, int value);

    public void setByte(String key, byte value);

    public void setShort(String key, short value);

    public void setLong(String key, long value);

    public void setFloat(String key, float value);

    public void setDouble(String key, double value);

    public void setBoolean(String key, boolean value);

    public void setString(String key, String value);

    public void setIntArray(String key, int[] value);

    public void setByteArray(String key, byte[] value);

    public void setSubTag(String key, WrappedNBTTagCompound subTag);

    public void setSubList(String key, WrappedNBTTagList subList);

    public WrappedNBTTagCompound getTagCompound(String key);

    public WrappedNBTTagList getTagList(String key, NBTTagType expectedListElements);

    public Object getValue(String key);

    public WrappedNBTTagCompound unmodifiable();

    public static class UnsupportedNBTTypeException extends Exception {}

}

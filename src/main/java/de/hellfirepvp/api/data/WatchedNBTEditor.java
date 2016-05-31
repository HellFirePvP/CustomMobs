package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.data.nbt.base.NBTTagType;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WatchedNBTEditor
 * Created by HellFirePvP
 * Date: 31.05.2016 / 10:39
 */
public interface WatchedNBTEditor {

    public void saveAndInvalidateTag();

    public ICustomMob getOwner();

    //Partially modifiable NBTTag

    public void removeKey(String key);

    public boolean hasKey(String key);

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

}

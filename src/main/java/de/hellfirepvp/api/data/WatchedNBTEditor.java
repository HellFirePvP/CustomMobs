package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

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

    //Query methods
    public void removeKey(String key);

    public boolean hasKey(String key);

    public Object getValue(String key);

    //Setter
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

    //Complex tags
    public void setItemStack(String key, ItemStack stack);

    //Recursive tags
    public APIWrappedNBTTagCompound createOrGetSubTag(String key);

    public APIWrappedNBTTagList createOrGetSubList(String key, NBTTagType expectedElementType);

    @Nullable
    public APIWrappedNBTTagCompound getTagCompound(String key);

    @Nullable
    public APIWrappedNBTTagList getTagList(String key, NBTTagType expectedListElements);

}

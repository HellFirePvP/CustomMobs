package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.NBTTagType;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
* This class is part of the CustomMobs Plugin
* The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
* Class: APIWrappedNBTTagCompound
* Created by HellFirePvP
* Date: 31.05.2016 / 13:24
*/
public interface APIWrappedNBTTagCompound extends WrappedNBTTagCompound {

    public void setItemStack(String key, ItemStack stack);

    public APIWrappedNBTTagCompound createSubTag(String key);

    public APIWrappedNBTTagList createSubList(String key, NBTTagType expectedElementType);

}

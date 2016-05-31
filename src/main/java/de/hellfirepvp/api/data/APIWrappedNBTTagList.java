package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.UnsupportedNBTTypeException;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
* This class is part of the CustomMobs Plugin
* The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
* Class: APIWrappedNBTTagList
* Created by HellFirePvP
* Date: 31.05.2016 / 13:23
*/
public interface APIWrappedNBTTagList extends WrappedNBTTagList {

    public void appendItemStack(ItemStack stack) throws UnsupportedNBTTypeException;

    public APIWrappedNBTTagCompound appendNewTagCompound() throws UnsupportedNBTTypeException;

    public APIWrappedNBTTagList appendNewTagList() throws UnsupportedNBTTypeException;

}

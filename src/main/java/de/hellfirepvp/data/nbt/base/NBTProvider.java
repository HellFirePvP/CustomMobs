package de.hellfirepvp.data.nbt.base;

import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTProvider
 * Created by HellFirePvP
 * Date: 24.05.2016 / 12:59
 */
public interface NBTProvider {

    public void saveStack(ItemStack stack, WrappedNBTTagCompound target);

    public ItemStack loadStack(WrappedNBTTagCompound savedStack);

    public WrappedNBTTagCompound newTagCompound();

    public WrappedNBTTagList newTagList();

    public void write(OutputStream stream, WrappedNBTTagCompound tag) throws IOException;

    public WrappedNBTTagCompound read(InputStream stream) throws IOException;

}

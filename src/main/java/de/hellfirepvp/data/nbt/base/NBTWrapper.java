package de.hellfirepvp.data.nbt.base;

import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTWrapper
 * Created by HellFirePvP
 * Date: 24.05.2016 / 00:07
 */
public class NBTWrapper {

    public static WrappedNBTTagCompound convertToTag(ItemStack stack) {
        if(NMSReflector.nbtProvider == null) return null;
        WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
        NMSReflector.nbtProvider.saveStack(stack, tag);
        return tag;
    }

    public static ItemStack reconstructFromTag(WrappedNBTTagCompound tag) {
        if(NMSReflector.nbtProvider == null) return null;
        return NMSReflector.nbtProvider.loadStack(tag);
    }

    public static void writeTagToOutputStream(WrappedNBTTagCompound tag, OutputStream stream) throws IOException {
        if(NMSReflector.nbtProvider == null) return;
        NMSReflector.nbtProvider.write(stream, tag);
    }

    public static WrappedNBTTagCompound readFromInputStream(InputStream stream) throws IOException {
        if(NMSReflector.nbtProvider == null) return null;
        return NMSReflector.nbtProvider.read(stream);
    }

}

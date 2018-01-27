package de.hellfirepvp.data.nbt.base;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import org.bukkit.inventory.ItemStack;

public class NBTWrapper
{
    public static WrappedNBTTagCompound convertToTag(final ItemStack stack) {
        if (NMSReflector.nbtProvider == null) {
            return null;
        }
        final WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
        NMSReflector.nbtProvider.saveStack(stack, tag);
        return tag;
    }
    
    public static ItemStack reconstructFromTag(final WrappedNBTTagCompound tag) {
        if (NMSReflector.nbtProvider == null) {
            return null;
        }
        return NMSReflector.nbtProvider.loadStack(tag);
    }
    
    public static void writeTagToOutputStream(final WrappedNBTTagCompound tag, final OutputStream stream) throws IOException {
        if (NMSReflector.nbtProvider == null) {
            return;
        }
        NMSReflector.nbtProvider.write(stream, tag);
    }
    
    public static WrappedNBTTagCompound readFromInputStream(final InputStream stream) throws IOException {
        if (NMSReflector.nbtProvider == null) {
            return null;
        }
        return NMSReflector.nbtProvider.read(stream);
    }
}

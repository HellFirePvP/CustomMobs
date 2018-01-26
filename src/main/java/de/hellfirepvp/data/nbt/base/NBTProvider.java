package de.hellfirepvp.data.nbt.base;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import org.bukkit.inventory.ItemStack;

public interface NBTProvider
{
    void saveStack(final ItemStack p0, final WrappedNBTTagCompound p1);
    
    ItemStack loadStack(final WrappedNBTTagCompound p0);
    
    WrappedNBTTagCompound newTagCompound();
    
    WrappedNBTTagList newTagList();
    
    void write(final OutputStream p0, final WrappedNBTTagCompound p1) throws IOException;
    
    WrappedNBTTagCompound read(final InputStream p0) throws IOException;
}

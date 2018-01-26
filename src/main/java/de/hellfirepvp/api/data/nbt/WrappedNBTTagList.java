package de.hellfirepvp.api.data.nbt;

import javax.annotation.Nullable;
import org.bukkit.inventory.ItemStack;

public interface WrappedNBTTagList extends Iterable<Object>
{
    Object getRawNMSTagList();
    
    boolean appendItemStack(final ItemStack p0);
    
    boolean appendNewElement(final Object p0);
    
    boolean appendTagCompound(final WrappedNBTTagCompound p0);
    
    boolean appendTagList(final WrappedNBTTagList p0);
    
    boolean hasElementType();
    
    NBTTagType getElementType();
    
    NullableIndexedElementIterator<Object> getElementIterator(final boolean p0);
    
    NullableIndexedElementIterator<Object> getElementIterator();
    
    @Nullable
    Object getElementAtIndex(final int p0);
    
    WrappedNBTTagList unmodifiable();
    
    int size();
}

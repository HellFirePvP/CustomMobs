package de.hellfirepvp.data.nbt.base;

import java.util.Iterator;
import de.hellfirepvp.api.data.nbt.NullableIndexedElementIterator;
import de.hellfirepvp.api.data.nbt.NBTTagType;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;

public class UnmodWrappedNBTTagList implements WrappedNBTTagList
{
    private final WrappedNBTTagList parent;
    
    public UnmodWrappedNBTTagList(final WrappedNBTTagList parent) {
        this.parent = parent;
    }
    
    @Override
    public Object getRawNMSTagList() {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public boolean appendItemStack(final ItemStack stack) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public boolean appendNewElement(final Object element) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public boolean appendTagCompound(final WrappedNBTTagCompound compound) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public boolean appendTagList(final WrappedNBTTagList list) {
        throw new UnsupportedOperationException("Unmodifiable");
    }
    
    @Override
    public boolean hasElementType() {
        return this.parent.hasElementType();
    }
    
    @Override
    public NBTTagType getElementType() {
        return this.parent.getElementType();
    }
    
    @Override
    public NullableIndexedElementIterator<Object> getElementIterator(final boolean unmodifiable) {
        return this.parent.getElementIterator(true);
    }
    
    @Override
    public NullableIndexedElementIterator<Object> getElementIterator() {
        return this.parent.getElementIterator(true);
    }
    
    @Override
    public Object getElementAtIndex(final int index) {
        final Object element = this.parent.getElementAtIndex(index);
        if (element == null) {
            return null;
        }
        if (element instanceof WrappedNBTTagCompound) {
            return ((WrappedNBTTagCompound)element).unmodifiable();
        }
        if (element instanceof WrappedNBTTagList) {
            return ((WrappedNBTTagList)element).unmodifiable();
        }
        return element;
    }
    
    @Override
    public WrappedNBTTagList unmodifiable() {
        return this;
    }
    
    @Override
    public int size() {
        return this.parent.size();
    }
    
    @Override
    public NullableIndexedElementIterator<Object> iterator() {
        return this.parent.getElementIterator(true);
    }
}

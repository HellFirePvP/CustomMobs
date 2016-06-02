package de.hellfirepvp.data.nbt.base;

import de.hellfirepvp.api.data.nbt.NBTTagType;
import de.hellfirepvp.api.data.nbt.NullableIndexedElementIterator;
import de.hellfirepvp.api.data.nbt.UnsupportedNBTTypeException;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: UnmodWrappedNBTTagList
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:18
 */
public class UnmodWrappedNBTTagList implements WrappedNBTTagList {

    private final WrappedNBTTagList parent;

    public UnmodWrappedNBTTagList(WrappedNBTTagList parent) {
        this.parent = parent;
    }

    @Override
    public Object getRawNMSTagList() {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public boolean appendItemStack(ItemStack stack) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public boolean appendNewElement(Object element) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public boolean appendTagCompound(WrappedNBTTagCompound compound) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public boolean appendTagList(WrappedNBTTagList list) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public boolean hasElementType() {
        return parent.hasElementType();
    }

    @Override
    public NBTTagType getElementType() {
        return parent.getElementType();
    }

    @Override
    public NullableIndexedElementIterator<Object> getElementIterator(boolean unmodifiable) { //Intentionally discarding 'unmodifiable' param here.
        return parent.getElementIterator(true);
    }

    @Override
    public NullableIndexedElementIterator<Object> getElementIterator() {
        return parent.getElementIterator(true);
    }

    @Override
    public Object getElementAtIndex(int index) {
        Object element = parent.getElementAtIndex(index);
        if(element == null) return null;
        if(element instanceof WrappedNBTTagCompound) {
            return ((WrappedNBTTagCompound) element).unmodifiable();
        } else if(element instanceof WrappedNBTTagList) {
            return ((WrappedNBTTagList) element).unmodifiable();
        }
        return element;
    }

    @Override
    public WrappedNBTTagList unmodifiable() {
        return this;
    }

    @Override
    public int size() {
        return parent.size();
    }

    @Override
    public NullableIndexedElementIterator<Object> iterator() {
        return parent.getElementIterator(true);
    }
}

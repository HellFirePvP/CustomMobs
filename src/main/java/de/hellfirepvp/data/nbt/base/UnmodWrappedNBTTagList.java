package de.hellfirepvp.data.nbt.base;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagList;

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
    public boolean appendNewElement(Object element) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public boolean appendTagCompound(WrappedNBTTagCompound compound) {
        throw new UnsupportedOperationException("Unmodifiable");
    }

    @Override
    public NBTTagType getElementType() {
        return parent.getElementType();
    }

    @Override
    public Iterator getImmutableElementIterator() {
        return parent.getImmutableElementIterator();
    }

    @Override
    public WrappedNBTTagList unmodifiable() {
        return this;
    }

    @Override
    public int size() {
        return parent.size();
    }
}

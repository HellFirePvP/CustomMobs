package de.hellfirepvp.api.data.nbt;

import de.hellfirepvp.data.nbt.base.NBTTagType;

import java.util.Iterator;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WrappedNBTTagList
 * Created by HellFirePvP
 * Date: 24.05.2016 / 13:19
 */
public interface WrappedNBTTagList extends Iterable<Object> {

    public Object getRawNMSTagList();

    public boolean appendNewElement(Object element);

    public boolean appendTagCompound(WrappedNBTTagCompound compound);

    public NBTTagType getElementType();

    public Iterator<Object> getElementIterator(boolean unmodifiable);

    public Iterator<Object> getElementIterator();

    public Object getElementAtIndex(int index);

    public WrappedNBTTagList unmodifiable();

    public int size();

}

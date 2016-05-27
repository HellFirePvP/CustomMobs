package de.hellfirepvp.data.nbt;

import java.util.Iterator;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WrappedNBTTagList
 * Created by HellFirePvP
 * Date: 24.05.2016 / 13:19
 */
public interface WrappedNBTTagList {

    public Object getRawNMSTagList();

    public boolean appendNewElement(Object element);

    public boolean appendTagCompound(WrappedNBTTagCompound compound);

    public NBTTagType getElementType();

    public Iterator getImmutableElementIterator();

    public int size();

}

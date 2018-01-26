package de.hellfirepvp.api.data.nbt;

import de.hellfirepvp.data.nbt.IndexedIterator;

import javax.annotation.Nullable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NullableElementIterator
 * Created by HellFirePvP
 * Date: 31.05.2016 / 14:21
 */
public interface NullableIndexedElementIterator<T> extends IndexedIterator<T> {

    @Nullable
    @Override
    public T next();

    @Override
    public void remove() throws UnsupportedOperationException;

}

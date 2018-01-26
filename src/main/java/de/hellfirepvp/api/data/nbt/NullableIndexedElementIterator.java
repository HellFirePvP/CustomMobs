package de.hellfirepvp.api.data.nbt;

import javax.annotation.Nullable;
import de.hellfirepvp.data.nbt.IndexedIterator;

public interface NullableIndexedElementIterator<T> extends IndexedIterator<T>
{
    @Nullable
    T next();
    
    void remove() throws UnsupportedOperationException;
}

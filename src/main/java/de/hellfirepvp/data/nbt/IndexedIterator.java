package de.hellfirepvp.data.nbt;

import java.util.Iterator;

public interface IndexedIterator<T> extends Iterator<T>
{
    int getCurrentIndex();
}

package de.hellfirepvp.data.nbt;

import java.util.Iterator;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: IndexedIterator
 * Created by HellFirePvP
 * Date: 31.05.2016 / 11:59
 */
public interface IndexedIterator<T> extends Iterator<T> {

    public int getCurrentIndex();

}

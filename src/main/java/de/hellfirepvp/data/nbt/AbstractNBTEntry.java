package de.hellfirepvp.data.nbt;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: AbstractNBTEntry
 * Created by HellFirePvP
 * Date: 29.05.2016 / 12:40
 */
public abstract class AbstractNBTEntry {

    private final NBTRegister.TypeRegister context;

    protected AbstractNBTEntry(NBTRegister.TypeRegister context) {
        this.context = context;
    }

    public abstract void registerEntries();

    public abstract String getMobTypeName();

    public final void offerEntry(String key, NBTEntryParser<?> entryParser) {
        context.setParser(key, entryParser);
    }

    protected final NBTRegister.TypeRegister getContext() {
        return context;
    }

}

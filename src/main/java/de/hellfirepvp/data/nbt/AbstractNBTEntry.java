package de.hellfirepvp.data.nbt;

import de.hellfirepvp.util.SupportedVersions;

public abstract class AbstractNBTEntry
{
    private final NBTRegister.TypeRegister context;
    
    protected AbstractNBTEntry(final NBTRegister.TypeRegister context) {
        this.context = context;
    }
    
    public abstract void registerEntries();
    
    public abstract String getMobTypeName();
    
    public final void offerEntry(final String key, final NBTEntryParser<?> entryParser) {
        this.context.setParser(key, entryParser);
    }
    
    public boolean isAvailable(final SupportedVersions mcVersion) {
        return true;
    }
    
    protected final NBTRegister.TypeRegister getContext() {
        return this.context;
    }
}

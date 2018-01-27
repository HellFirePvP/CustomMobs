package de.hellfirepvp.data.nbt.entries.base;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;

public abstract class NBTEntryTameable extends NBTEntryAgeable
{
    protected NBTEntryTameable(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("OwnerUUID", NBTEntryParser.STRING_PARSER);
        this.offerEntry("Sitting", NBTEntryParser.BOOLEAN_PARSER);
    }
}

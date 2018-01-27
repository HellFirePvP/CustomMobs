package de.hellfirepvp.data.nbt.entries.base;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;

public abstract class NBTEntryAgeable extends NBTEntryLivingEntity
{
    protected NBTEntryAgeable(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("InLove", NBTEntryParser.INT_PARSER);
        this.offerEntry("Age", NBTEntryParser.INT_PARSER);
        this.offerEntry("ForcedAge", NBTEntryParser.INT_PARSER);
    }
}

package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryEnderman extends NBTEntryLivingEntity
{
    public NBTEntryEnderman(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("carried", NBTEntryParser.SHORT_PARSER);
        this.offerEntry("carriedData", NBTEntryParser.SHORT_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Enderman";
    }
}

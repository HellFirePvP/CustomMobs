package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

public class NBTEntrySheep extends NBTEntryAgeable
{
    public NBTEntrySheep(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("Sheared", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Color", NBTEntryParser.BYTE_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Sheep";
    }
}

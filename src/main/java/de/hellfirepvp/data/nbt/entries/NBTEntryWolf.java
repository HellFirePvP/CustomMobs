package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryTameable;

public class NBTEntryWolf extends NBTEntryTameable
{
    public NBTEntryWolf(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("Angry", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("CollarColor", NBTEntryParser.BYTE_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Wolf";
    }
}

package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryTameable;

public class NBTEntryOzelot extends NBTEntryTameable
{
    public NBTEntryOzelot(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("CatType", NBTEntryParser.INT_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Ozelot";
    }
}

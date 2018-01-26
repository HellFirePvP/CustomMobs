package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

public class NBTEntryCow extends NBTEntryAgeable
{
    public NBTEntryCow(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public String getMobTypeName() {
        return "Cow";
    }
}

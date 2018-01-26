package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTRegister;

public class NBTEntryMule extends NBTEntryDonkey
{
    public NBTEntryMule(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public String getMobTypeName() {
        return "Mule";
    }
}

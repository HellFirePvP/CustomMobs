package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryBlaze extends NBTEntryLivingEntity
{
    public NBTEntryBlaze(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public String getMobTypeName() {
        return "Blaze";
    }
}

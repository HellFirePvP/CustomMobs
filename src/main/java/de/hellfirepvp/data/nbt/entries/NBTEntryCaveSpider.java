package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryCaveSpider extends NBTEntryLivingEntity
{
    public NBTEntryCaveSpider(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public String getMobTypeName() {
        return "CaveSpider";
    }
}

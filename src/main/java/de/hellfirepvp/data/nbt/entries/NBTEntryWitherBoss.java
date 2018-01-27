package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryWitherBoss extends NBTEntryLivingEntity
{
    public NBTEntryWitherBoss(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("Invul", NBTEntryParser.INT_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "WitherBoss";
    }
}

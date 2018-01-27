package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntrySlime extends NBTEntryLivingEntity
{
    public NBTEntrySlime(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("Size", NBTEntryParser.INT_PARSER);
        this.offerEntry("wasOnGround", NBTEntryParser.BOOLEAN_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Slime";
    }
}

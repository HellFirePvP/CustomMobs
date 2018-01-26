package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryVillagerGolem extends NBTEntryLivingEntity
{
    public NBTEntryVillagerGolem(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("PlayerCreated", NBTEntryParser.BOOLEAN_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "VillagerGolem";
    }
}

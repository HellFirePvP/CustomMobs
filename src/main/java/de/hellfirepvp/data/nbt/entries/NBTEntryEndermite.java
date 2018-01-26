package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryEndermite extends NBTEntryLivingEntity
{
    public NBTEntryEndermite(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("Lifetime", NBTEntryParser.INT_PARSER);
        this.offerEntry("PlayerSpawned", NBTEntryParser.BYTE_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Endermite";
    }
}

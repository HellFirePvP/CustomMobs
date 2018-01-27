package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryCreeper extends NBTEntryLivingEntity
{
    public NBTEntryCreeper(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("powered", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("ExplosionRadius", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Fuse", NBTEntryParser.SHORT_PARSER);
        this.offerEntry("ignited", NBTEntryParser.BOOLEAN_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Creeper";
    }
}

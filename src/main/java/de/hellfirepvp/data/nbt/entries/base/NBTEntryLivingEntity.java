package de.hellfirepvp.data.nbt.entries.base;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.AbstractNBTEntry;

public abstract class NBTEntryLivingEntity extends AbstractNBTEntry
{
    protected NBTEntryLivingEntity(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        this.offerEntry("FallDistance", NBTEntryParser.FLOAT_PARSER);
        this.offerEntry("Fire", NBTEntryParser.SHORT_PARSER);
        this.offerEntry("Air", NBTEntryParser.SHORT_PARSER);
        this.offerEntry("OnGround", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Invulnerable", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("PortalCooldown", NBTEntryParser.INT_PARSER);
        this.offerEntry("CustomName", NBTEntryParser.STRING_PARSER);
        this.offerEntry("CustomNameVisible", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Silent", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Glowing", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Health", NBTEntryParser.FLOAT_PARSER);
        this.offerEntry("AbsorptionAmount", NBTEntryParser.FLOAT_PARSER);
        this.offerEntry("HurtTime", NBTEntryParser.SHORT_PARSER);
        this.offerEntry("NoAI", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("PersistenceRequired", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("LeftHanded", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Team", NBTEntryParser.STRING_PARSER);
    }
}

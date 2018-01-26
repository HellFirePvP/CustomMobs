package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryPigZombie extends NBTEntryLivingEntity
{
    public NBTEntryPigZombie(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("IsVillager", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("IsBaby", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("ConversionTime", NBTEntryParser.INT_PARSER);
        this.offerEntry("CanBreakDoors", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("VillagerProfession", NBTEntryParser.INT_PARSER);
        this.offerEntry("Anger", NBTEntryParser.SHORT_PARSER);
        this.offerEntry("HurtBy", NBTEntryParser.STRING_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "PigZombie";
    }
}

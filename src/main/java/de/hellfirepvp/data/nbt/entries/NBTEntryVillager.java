package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

public class NBTEntryVillager extends NBTEntryAgeable
{
    public NBTEntryVillager(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("Profession", NBTEntryParser.INT_PARSER);
        this.offerEntry("Riches", NBTEntryParser.INT_PARSER);
        this.offerEntry("Career", NBTEntryParser.INT_PARSER);
        this.offerEntry("CareerLevel", NBTEntryParser.INT_PARSER);
        this.offerEntry("Willing", NBTEntryParser.BOOLEAN_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Villager";
    }
}

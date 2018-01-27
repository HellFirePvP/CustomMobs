package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;

public class NBTEntryDonkey extends NBTEntryEntityHorse
{
    public NBTEntryDonkey(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("ChestedHorse", NBTEntryParser.BOOLEAN_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Donkey";
    }
}

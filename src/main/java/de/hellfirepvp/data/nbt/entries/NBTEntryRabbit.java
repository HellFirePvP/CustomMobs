package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

public class NBTEntryRabbit extends NBTEntryAgeable
{
    public NBTEntryRabbit(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("RabbitType", NBTEntryParser.INT_PARSER);
        this.offerEntry("MoreCarrotTicks", NBTEntryParser.INT_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Rabbit";
    }
}

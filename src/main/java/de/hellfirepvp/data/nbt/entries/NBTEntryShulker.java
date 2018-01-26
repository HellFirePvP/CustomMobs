package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryShulker extends NBTEntryLivingEntity
{
    public NBTEntryShulker(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("Peek", NBTEntryParser.BYTE_PARSER);
        this.offerEntry("AttachFace", NBTEntryParser.BYTE_PARSER);
        this.offerEntry("APX", NBTEntryParser.INT_PARSER);
        this.offerEntry("APY", NBTEntryParser.INT_PARSER);
        this.offerEntry("APZ", NBTEntryParser.INT_PARSER);
    }
    
    @Override
    public String getMobTypeName() {
        return "Shulker";
    }
}

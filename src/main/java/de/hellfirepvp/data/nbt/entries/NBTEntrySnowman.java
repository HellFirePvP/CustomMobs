package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntrySnowman extends NBTEntryLivingEntity
{
    public NBTEntrySnowman(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        if (CustomMobs.currentVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_12_R1)) {
            this.offerEntry("Pumpkin", NBTEntryParser.BOOLEAN_PARSER);
        }
    }
    
    @Override
    public String getMobTypeName() {
        return "SnowMan";
    }
}

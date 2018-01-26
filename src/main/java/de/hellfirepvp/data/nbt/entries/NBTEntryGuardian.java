package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryGuardian extends NBTEntryLivingEntity
{
    public NBTEntryGuardian(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        if (!CustomMobs.currentVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_12_R1)) {
            this.offerEntry("Elder", NBTEntryParser.BOOLEAN_PARSER);
        }
    }
    
    @Override
    public String getMobTypeName() {
        return "Guardian";
    }
}

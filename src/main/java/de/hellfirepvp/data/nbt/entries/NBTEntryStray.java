package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryStray extends NBTEntryLivingEntity
{
    public NBTEntryStray(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public boolean isAvailable(final SupportedVersions mcVersion) {
        return mcVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_12_R1);
    }
    
    @Override
    public String getMobTypeName() {
        return "Stray";
    }
}

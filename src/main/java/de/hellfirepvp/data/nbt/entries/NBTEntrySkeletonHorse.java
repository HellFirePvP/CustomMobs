package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

public class NBTEntrySkeletonHorse extends NBTEntryAgeable
{
    public NBTEntrySkeletonHorse(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("SkeletonTrap", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("SkeletonTrapTime", NBTEntryParser.INT_PARSER);
    }
    
    @Override
    public boolean isAvailable(final SupportedVersions mcVersion) {
        return mcVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_12_R1);
    }
    
    @Override
    public String getMobTypeName() {
        return "SkeletonHorse";
    }
}

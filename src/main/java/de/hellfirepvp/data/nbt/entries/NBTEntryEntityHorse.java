package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

public class NBTEntryEntityHorse extends NBTEntryAgeable
{
    public NBTEntryEntityHorse(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("Bred", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("EatingHaystack", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Tame", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("Temper", NBTEntryParser.INT_PARSER);
        this.offerEntry("Variant", NBTEntryParser.INT_PARSER);
        this.offerEntry("OwnerUUID", NBTEntryParser.STRING_PARSER);
        this.offerEntry("Saddle", NBTEntryParser.BOOLEAN_PARSER);
        if (!CustomMobs.currentVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_12_R1)) {
            this.offerEntry("SkeletonTrap", NBTEntryParser.BOOLEAN_PARSER);
            this.offerEntry("SkeletonTrapTime", NBTEntryParser.INT_PARSER);
            this.offerEntry("ChestedHorse", NBTEntryParser.BOOLEAN_PARSER);
            this.offerEntry("Type", NBTEntryParser.INT_PARSER);
            this.offerEntry("HasReproduced", NBTEntryParser.BOOLEAN_PARSER);
        }
    }
    
    @Override
    public boolean isAvailable(final SupportedVersions mcVersion) {
        return mcVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_12_R1);
    }
    
    @Override
    public String getMobTypeName() {
        return "EntityHorse";
    }
}

package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

public class NBTEntryZombie extends NBTEntryLivingEntity
{
    public NBTEntryZombie(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("IsBaby", NBTEntryParser.BOOLEAN_PARSER);
        this.offerEntry("CanBreakDoors", NBTEntryParser.BOOLEAN_PARSER);
        if (!CustomMobs.currentVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_11_R1)) {
            if (CustomMobs.currentVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_10_R1)) {
                this.offerEntry("ConversionTime", NBTEntryParser.INT_PARSER);
                this.offerEntry("ZombieType", NBTEntryParser.INT_PARSER);
            }
            else {
                this.offerEntry("VillagerProfession", NBTEntryParser.INT_PARSER);
                this.offerEntry("IsVillager", NBTEntryParser.BOOLEAN_PARSER);
            }
        }
    }
    
    @Override
    public String getMobTypeName() {
        return "Zombie";
    }
}

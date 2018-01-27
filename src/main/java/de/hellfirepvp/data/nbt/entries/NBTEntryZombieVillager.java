package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;

public class NBTEntryZombieVillager extends NBTEntryZombie
{
    public NBTEntryZombieVillager(final NBTRegister.TypeRegister context) {
        super(context);
    }
    
    @Override
    public void registerEntries() {
        super.registerEntries();
        this.offerEntry("ConversionTime", NBTEntryParser.INT_PARSER);
        this.offerEntry("Profession", NBTEntryParser.INT_PARSER);
    }
    
    @Override
    public boolean isAvailable(final SupportedVersions mcVersion) {
        return mcVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_11_R1);
    }
    
    @Override
    public String getMobTypeName() {
        return "ZombieVillager";
    }
}

package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryZombie
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:13
 */
public class NBTEntryZombie extends NBTEntryLivingEntity {

    public NBTEntryZombie(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("IsVillager", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("IsBaby", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("ConversionTime", NBTEntryParser.INT_PARSER);
        offerEntry("CanBreakDoors", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("VillagerProfession", NBTEntryParser.INT_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Zombie";
    }
}

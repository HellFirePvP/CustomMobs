package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryPigZombie
 * Created by HellFirePvP
 * Date: 29.05.2016 / 16:56
 */
public class NBTEntryPigZombie extends NBTEntryLivingEntity {

    public NBTEntryPigZombie(NBTRegister.TypeRegister context) {
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
        offerEntry("Anger", NBTEntryParser.SHORT_PARSER);
        offerEntry("HurtBy", NBTEntryParser.STRING_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "PigZombie";
    }
}

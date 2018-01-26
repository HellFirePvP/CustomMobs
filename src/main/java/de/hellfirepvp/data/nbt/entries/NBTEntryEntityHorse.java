package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryEntityHorse
 * Created by HellFirePvP
 * Date: 29.05.2016 / 15:05
 */
public class NBTEntryEntityHorse extends NBTEntryAgeable {

    public NBTEntryEntityHorse(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Bred", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("ChestedHorse", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("EatingHaystack", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("HasReproduced", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("Tame", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("Temper", NBTEntryParser.INT_PARSER);
        offerEntry("Type", NBTEntryParser.INT_PARSER);
        offerEntry("Variant", NBTEntryParser.INT_PARSER);
        offerEntry("OwnerUUID", NBTEntryParser.STRING_PARSER);
        offerEntry("Saddle", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("SkeletonTrap", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("SkeletonTrapTime", NBTEntryParser.INT_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "EntityHorse";
    }
}

package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryVillager
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:08
 */
public class NBTEntryVillager extends NBTEntryAgeable {

    public NBTEntryVillager(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Profession", NBTEntryParser.INT_PARSER);
        offerEntry("Riches", NBTEntryParser.INT_PARSER);
        offerEntry("Career", NBTEntryParser.INT_PARSER);
        offerEntry("CareerLevel", NBTEntryParser.INT_PARSER);
        offerEntry("Willing", NBTEntryParser.BOOLEAN_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Villager";
    }
}

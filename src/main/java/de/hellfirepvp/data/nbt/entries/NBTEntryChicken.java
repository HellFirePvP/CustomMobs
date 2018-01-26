package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryChicken
 * Created by HellFirePvP
 * Date: 29.05.2016 / 14:55
 */
public class NBTEntryChicken extends NBTEntryAgeable {

    public NBTEntryChicken(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("IsChickenJockey", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("EggLayTime", NBTEntryParser.INT_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Chicken";
    }
}

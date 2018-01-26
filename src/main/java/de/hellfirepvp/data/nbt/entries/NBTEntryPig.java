package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryPig
 * Created by HellFirePvP
 * Date: 29.05.2016 / 16:56
 */
public class NBTEntryPig extends NBTEntryAgeable {

    public NBTEntryPig(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Saddle", NBTEntryParser.BOOLEAN_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Pig";
    }
}

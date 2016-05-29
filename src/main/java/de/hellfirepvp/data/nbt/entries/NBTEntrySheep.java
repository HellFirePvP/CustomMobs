package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntrySheep
 * Created by HellFirePvP
 * Date: 29.05.2016 / 16:59
 */
public class NBTEntrySheep extends NBTEntryAgeable {

    public NBTEntrySheep(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Sheared", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("Color", NBTEntryParser.BYTE_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Sheep";
    }
}

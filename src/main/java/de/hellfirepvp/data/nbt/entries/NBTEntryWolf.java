package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryTameable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryWolf
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:12
 */
public class NBTEntryWolf extends NBTEntryTameable {

    public NBTEntryWolf(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Angry", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("CollarColor", NBTEntryParser.BYTE_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Wolf";
    }
}

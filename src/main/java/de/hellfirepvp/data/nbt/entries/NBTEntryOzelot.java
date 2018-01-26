package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryTameable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryOzelot
 * Created by HellFirePvP
 * Date: 29.05.2016 / 16:55
 */
public class NBTEntryOzelot extends NBTEntryTameable {

    public NBTEntryOzelot(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("CatType", NBTEntryParser.INT_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Ozelot";
    }
}

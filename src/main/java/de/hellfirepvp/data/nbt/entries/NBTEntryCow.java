package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryCow
 * Created by HellFirePvP
 * Date: 29.05.2016 / 14:56
 */
public class NBTEntryCow extends NBTEntryAgeable {

    public NBTEntryCow(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public String getMobTypeName() {
        return "Cow";
    }
}

package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryMushroomCow
 * Created by HellFirePvP
 * Date: 29.05.2016 / 16:52
 */
public class NBTEntryMushroomCow extends NBTEntryAgeable {

    public NBTEntryMushroomCow(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public String getMobTypeName() {
        return "MushroomCow";
    }
}

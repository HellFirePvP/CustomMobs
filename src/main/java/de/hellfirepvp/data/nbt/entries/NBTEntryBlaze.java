package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryBlaze
 * Created by HellFirePvP
 * Date: 29.05.2016 / 14:54
 */
public class NBTEntryBlaze extends NBTEntryLivingEntity {

    public NBTEntryBlaze(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public String getMobTypeName() {
        return "Blaze";
    }
}

package de.hellfirepvp.data.nbt;

import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntrySnowMan
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:06
 */
public class NBTEntrySnowMan extends NBTEntryLivingEntity {

    public NBTEntrySnowMan(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public String getMobTypeName() {
        return "SnowMan";
    }
}

package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryCaveSpider
 * Created by HellFirePvP
 * Date: 29.05.2016 / 14:54
 */
public class NBTEntryCaveSpider extends NBTEntryLivingEntity {

    public NBTEntryCaveSpider(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public String getMobTypeName() {
        return "CaveSpider";
    }
}

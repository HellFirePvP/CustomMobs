package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryGuardian
 * Created by HellFirePvP
 * Date: 29.05.2016 / 16:50
 */
public class NBTEntryGuardian extends NBTEntryLivingEntity {

    public NBTEntryGuardian(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Elder", NBTEntryParser.BOOLEAN_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Guardian";
    }
}
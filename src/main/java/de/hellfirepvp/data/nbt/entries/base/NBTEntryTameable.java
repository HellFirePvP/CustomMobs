package de.hellfirepvp.data.nbt.entries.base;

import de.hellfirepvp.data.nbt.NBTRegister;

import static de.hellfirepvp.data.nbt.NBTEntryParser.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryTameable
 * Created by HellFirePvP
 * Date: 29.05.2016 / 14:44
 */
public abstract class NBTEntryTameable extends NBTEntryAgeable {

    protected NBTEntryTameable(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        //Std. Tameable stuff
        offerEntry("OwnerUUID", STRING_PARSER);
        offerEntry("Sitting", BOOLEAN_PARSER);
    }
}

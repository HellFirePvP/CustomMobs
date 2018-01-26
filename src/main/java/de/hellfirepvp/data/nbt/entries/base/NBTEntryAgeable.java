package de.hellfirepvp.data.nbt.entries.base;

import de.hellfirepvp.data.nbt.NBTRegister;

import static de.hellfirepvp.data.nbt.NBTEntryParser.*;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryAgeable
 * Created by HellFirePvP
 * Date: 29.05.2016 / 14:42
 */
public abstract class NBTEntryAgeable extends NBTEntryLivingEntity {

    protected NBTEntryAgeable(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        //Std. Ageable stuff
        offerEntry("InLove", INT_PARSER);
        offerEntry("Age", INT_PARSER);
        offerEntry("ForcedAge", INT_PARSER);
    }
}

package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryEnderman
 * Created by HellFirePvP
 * Date: 29.05.2016 / 15:01
 */
public class NBTEntryEnderman extends NBTEntryLivingEntity {

    public NBTEntryEnderman(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("carried", NBTEntryParser.SHORT_PARSER);
        offerEntry("carriedData", NBTEntryParser.SHORT_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Enderman";
    }
}

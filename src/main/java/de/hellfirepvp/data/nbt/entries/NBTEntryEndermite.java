package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryEndermite
 * Created by HellFirePvP
 * Date: 29.05.2016 / 15:03
 */
public class NBTEntryEndermite extends NBTEntryLivingEntity {

    public NBTEntryEndermite(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Lifetime", NBTEntryParser.INT_PARSER);
        offerEntry("PlayerSpawned", NBTEntryParser.BYTE_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Endermite";
    }
}

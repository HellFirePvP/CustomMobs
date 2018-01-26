package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntrySlime
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:05
 */
public class NBTEntrySlime extends NBTEntryLivingEntity {

    public NBTEntrySlime(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Size", NBTEntryParser.INT_PARSER);
        offerEntry("wasOnGround", NBTEntryParser.BOOLEAN_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Slime";
    }
}

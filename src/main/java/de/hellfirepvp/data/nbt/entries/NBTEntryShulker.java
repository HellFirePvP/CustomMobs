package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryShulker
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:00
 */
public class NBTEntryShulker extends NBTEntryLivingEntity {

    public NBTEntryShulker(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("Peek", NBTEntryParser.BYTE_PARSER);
        offerEntry("AttachFace", NBTEntryParser.BYTE_PARSER);
        offerEntry("APX", NBTEntryParser.INT_PARSER);
        offerEntry("APY", NBTEntryParser.INT_PARSER);
        offerEntry("APZ", NBTEntryParser.INT_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Shulker";
    }
}

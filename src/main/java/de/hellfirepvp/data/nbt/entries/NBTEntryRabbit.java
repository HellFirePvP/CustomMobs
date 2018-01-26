package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryAgeable;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryRabbit
 * Created by HellFirePvP
 * Date: 29.05.2016 / 16:58
 */
public class NBTEntryRabbit extends NBTEntryAgeable {

    public NBTEntryRabbit(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("RabbitType", NBTEntryParser.INT_PARSER);
        offerEntry("MoreCarrotTicks", NBTEntryParser.INT_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Rabbit";
    }
}

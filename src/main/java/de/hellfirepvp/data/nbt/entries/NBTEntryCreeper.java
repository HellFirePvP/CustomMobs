package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryCreeper
 * Created by HellFirePvP
 * Date: 29.05.2016 / 14:57
 */
public class NBTEntryCreeper extends NBTEntryLivingEntity {

    public NBTEntryCreeper(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("powered", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("ExplosionRadius", NBTEntryParser.BOOLEAN_PARSER);
        offerEntry("Fuse", NBTEntryParser.SHORT_PARSER);
        offerEntry("ignited", NBTEntryParser.BOOLEAN_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Creeper";
    }
}

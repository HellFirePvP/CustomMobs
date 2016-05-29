package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntryVillagerGolem
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:10
 */
public class NBTEntryVillagerGolem extends NBTEntryLivingEntity {

    public NBTEntryVillagerGolem(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("PlayerCreated", NBTEntryParser.BOOLEAN_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "VillagerGolem";
    }
}

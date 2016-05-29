package de.hellfirepvp.data.nbt.entries;

import de.hellfirepvp.data.nbt.NBTEntryParser;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.data.nbt.entries.base.NBTEntryLivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NBTEntrySkeleton
 * Created by HellFirePvP
 * Date: 29.05.2016 / 17:05
 */
public class NBTEntrySkeleton extends NBTEntryLivingEntity {

    public NBTEntrySkeleton(NBTRegister.TypeRegister context) {
        super(context);
    }

    @Override
    public void registerEntries() {
        super.registerEntries();

        offerEntry("SkeletonType", NBTEntryParser.BYTE_PARSER);
    }

    @Override
    public String getMobTypeName() {
        return "Skeleton";
    }
}

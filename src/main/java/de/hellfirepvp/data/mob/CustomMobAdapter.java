package de.hellfirepvp.data.mob;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.exception.SpawnLimitException;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CustomMobAdapter
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:22
 */
public class CustomMobAdapter implements ICustomMob {

    private final CustomMob parent;

    CustomMobAdapter(CustomMob parent) {
        this.parent = parent;
    }

    @Override
    public WrappedNBTTagCompound getReadOnlyTag() {
        return parent.getDataSnapshot().unmodifiable();
    }

    @Override
    public String getName() {
        return parent.getMobFileName();
    }

    @Override
    public EntityType getEntityType() {
        return parent.getEntityAdapter().getEntityType();
    }

    @Override
    public LivingEntity spawnAt(Location at) throws SpawnLimitException {
        return parent.spawnAt(at);
    }

}

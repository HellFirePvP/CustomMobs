package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.exception.SpawnLimitException;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ICustomMob
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:09
 */
public interface ICustomMob {

    public WrappedNBTTagCompound getUnmodifiableTag();

    public String getName();

    public EntityType getEntityType();

    public LivingEntity spawnAt(Location at) throws SpawnLimitException;

}

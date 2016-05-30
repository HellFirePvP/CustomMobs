package de.hellfirepvp.nms;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: MobTypeProvider
 * Created by HellFirePvP
 * Date: 24.05.2016 / 14:37
 */
public interface MobTypeProvider {

    public void discoverMobTypes();

    public List<String> getTypeNames();

    public LivingEntity getEntityForName(World world, String typeName);

    public LivingEntity createEntityShell(World world, WrappedNBTTagCompound data);

    public LivingEntity spawnEntityFromSerializedData(Location at, WrappedNBTTagCompound data);

    public WrappedNBTTagCompound getDataFromEntity(LivingEntity le);

}

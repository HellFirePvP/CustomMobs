package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.exception.SpawnLimitException;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ICustomMob
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:09
 */
public interface ICustomMob {

    public WrappedNBTTagCompound getReadOnlyTag();

    public String getName();

    public EntityType getEntityType();

    public LivingEntity spawnAt(Location at) throws SpawnLimitException;

    //Less important stuff

    public Double getMaxHealth();

    public Integer getBurnTime();

    public Integer getExperienceDrop();

    public String getDisplayName();

    public String getCommandLine();

    public Integer getSpawnLimit();

    public Boolean isFireProof();

    public Collection<PotionEffect> getPotionEffects();

    public Map<EquipmentSlot, ItemStack> getEquipment();

    public Map<ItemStack, Double> getDrops();

}

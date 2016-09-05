package de.hellfirepvp.api.data;

import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.exception.SpawnLimitException;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ICustomMob
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:09
 */
public interface ICustomMob {

    /**
     * The WatchedNBTEditor is a buffering-setonly NBT-tag editor.
     * Unless you call "saveAndInvalidateTag" after doing your changes, nothing will be applied to the NBT Tag.
     *
     * @return Set-only buffering NBT tag.
     */
    public WatchedNBTEditor editNBTTag();

    /**
     * The WrappedNBTTagCompound returned here only allows read operations.
     * It contains all data that all living entities of this custommob share.
     *
     * @return a read-only NBT tag.
     */
    public WrappedNBTTagCompound getReadOnlyTag();

    /**
     * @return the name of the custommob
     */
    public String getName();

    /**
     * @return the entitytype of the custommob
     */
    public EntityType getEntityType();

    /**
     * Tries to spawn the custommob at a specific location.
     *
     * @param at the location to spawn the mob at.
     * @return The entity spawned
     * @throws SpawnLimitException In case the mob's spawnlimit has been hit.
     */
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

    public List<ItemDrop> getDrops();

    public static class ItemDrop {

        private final ItemStack stack;
        private final double chance;

        public ItemDrop(ItemStack stack, double chance) {
            this.stack = stack;
            this.chance = chance;
        }

        public ItemStack getStack() {
            return stack;
        }

        public double getChance() {
            return chance;
        }

    }

}

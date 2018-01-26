package de.hellfirepvp.api.data;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import java.util.Map;
import org.bukkit.potion.PotionEffect;
import java.util.Collection;
import de.hellfirepvp.api.exception.SpawnLimitException;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;

public interface ICustomMob
{
    WatchedNBTEditor editNBTTag();
    
    WrappedNBTTagCompound getReadOnlyTag();
    
    String getName();
    
    EntityType getEntityType();
    
    LivingEntity spawnAt(final Location p0) throws SpawnLimitException;
    
    Double getMaxHealth();
    
    Integer getBurnTime();
    
    @Deprecated
    Integer getExperienceDrop();
    
    Integer getLowestExperienceDrop();
    
    Integer getHighestExperienceDrop();
    
    String getDisplayName();
    
    String getCommandLine();
    
    Integer getSpawnLimit();
    
    Boolean isFireProof();
    
    Collection<PotionEffect> getPotionEffects();
    
    Map<EquipmentSlot, ItemStack> getEquipment();
    
    List<ItemDrop> getDrops();
    
    public static class ItemDrop
    {
        private final ItemStack stack;
        private final double chance;
        
        public ItemDrop(final ItemStack stack, final double chance) {
            this.stack = stack;
            this.chance = chance;
        }
        
        public ItemStack getStack() {
            return this.stack;
        }
        
        public double getChance() {
            return this.chance;
        }
    }
}

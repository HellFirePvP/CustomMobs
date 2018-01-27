package de.hellfirepvp.data.mob;

import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.api.data.EquipmentSlot;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.ChatColor;
import org.spigotmc.SpigotConfig;
import de.hellfirepvp.util.ServerType;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import java.lang.ref.WeakReference;
import org.bukkit.Bukkit;
import org.bukkit.World;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.entity.LivingEntity;

public class EntityAdapter
{
    private final CustomMob parentMob;
    private LivingEntity adapterEntity;
    
    public EntityAdapter(final CustomMob customMob) {
        this.parentMob = customMob;
        this.adapterEntity = NMSReflector.mobTypeProvider.createEntityShell(getDefaultWorld(), customMob.getDataSnapshot());
    }
    
    public WrappedNBTTagCompound getEntityTag() {
        final WrappedNBTTagCompound tag = this.parentMob.getDataSnapshot();
        NMSReflector.mobTypeProvider.mergeEntityDataInto(this.adapterEntity, tag);
        final WrappedNBTTagCompound cmobTag = this.parentMob.getDataAdapter().getPersistentCustomMobsTag();
        tag.setSubTag("CustomMobs", cmobTag);
        return tag;
    }
    
    public void reloadEntity() {
        this.adapterEntity.remove();
        this.adapterEntity = NMSReflector.mobTypeProvider.createEntityShell(getDefaultWorld(), this.parentMob.getDataSnapshot());
    }
    
    public static World getDefaultWorld() {
        final World w = Bukkit.getWorlds().get(0);
        if (w == null) {
            throw new IllegalStateException("No worlds are loaded?");
        }
        return w;
    }
    
    public WeakReference<LivingEntity> getAdapterEntity() {
        return new WeakReference<LivingEntity>(this.adapterEntity);
    }
    
    public EntityType getEntityType() {
        return this.adapterEntity.getType();
    }
    
    public boolean setBaby() {
        if (this.adapterEntity instanceof Ageable) {
            ((Ageable)this.adapterEntity).setBaby();
            this.parentMob.updateTag();
            return true;
        }
        if (this.adapterEntity instanceof Zombie) {
            ((Zombie)this.adapterEntity).setBaby(true);
            this.parentMob.updateTag();
            return true;
        }
        return false;
    }
    
    public boolean setAdult() {
        if (this.adapterEntity instanceof Ageable) {
            ((Ageable)this.adapterEntity).setBaby();
            this.parentMob.updateTag();
            return true;
        }
        if (this.adapterEntity instanceof Zombie) {
            ((Zombie)this.adapterEntity).setBaby(false);
            this.parentMob.updateTag();
            return true;
        }
        return false;
    }
    
    public boolean setHealth(final double health) {
        if (ServerType.getResolvedType() == ServerType.SPIGOT && health > SpigotConfig.maxHealth) {
            return false;
        }
        this.adapterEntity.setMaxHealth(health);
        this.adapterEntity.setHealth(health);
        this.parentMob.updateTag();
        return true;
    }
    
    public void setFireTicks(final int fireTicks) {
        this.adapterEntity.setFireTicks(fireTicks);
        this.parentMob.updateTag();
    }
    
    public void setCustomName(String name) {
        name = ChatColor.translateAlternateColorCodes('&', name);
        this.adapterEntity.setCustomNameVisible(true);
        this.adapterEntity.setCustomName(name);
        this.parentMob.updateTag();
    }
    
    public void addPotionEffect(final PotionEffect... effects) {
        for (final PotionEffect pe : effects) {
            if (pe != null) {
                this.adapterEntity.addPotionEffect(pe);
            }
        }
        this.parentMob.updateTag();
    }
    
    public void removePotionEffect(final PotionEffectType type) {
        this.adapterEntity.removePotionEffect(type);
        this.parentMob.updateTag();
    }
    
    public void clearPotionEffects() {
        for (final PotionEffect pe : this.adapterEntity.getActivePotionEffects()) {
            this.adapterEntity.removePotionEffect(pe.getType());
        }
        this.parentMob.updateTag();
    }
    
    public double getMaxHealth() {
        return this.adapterEntity.getMaxHealth();
    }
    
    public int getFireTicks() {
        return this.adapterEntity.getFireTicks();
    }
    
    public String getCustomName() {
        return this.adapterEntity.getCustomName();
    }
    
    public Collection<PotionEffect> getAcivePotionEffects() {
        return (Collection<PotionEffect>)this.adapterEntity.getActivePotionEffects();
    }
    
    public ItemStack getEquipment(final EquipmentSlot slot) {
        return slot.getEquipment(this.adapterEntity.getEquipment());
    }
    
    public void setEquipment(final EquipmentSlot slot, final ItemStack item) {
        slot.setEquipment(item, this.adapterEntity.getEquipment());
        this.parentMob.updateTag();
    }
}

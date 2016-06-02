package de.hellfirepvp.data.mob;

import de.hellfirepvp.api.data.EquipmentSlot;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.SpigotConfig;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: EntityAdapter
 * Created by HellFirePvP
 * Date: 26.05.2016 / 14:32
 */
public class EntityAdapter {

    private final CustomMob parentMob;
    private LivingEntity adapterEntity;

    public EntityAdapter(CustomMob customMob) {
        this.parentMob = customMob;
        this.adapterEntity = NMSReflector.mobTypeProvider.createEntityShell(getDefaultWorld(), customMob.getDataSnapshot());
    }

    public WrappedNBTTagCompound getEntityTag() {
        WrappedNBTTagCompound tag = parentMob.getDataSnapshot();
        NMSReflector.mobTypeProvider.mergeEntityDataInto(adapterEntity, tag);
        WrappedNBTTagCompound cmobTag = parentMob.getDataAdapter().getPersistentCustomMobsTag();
        tag.setSubTag("CustomMobs", cmobTag);
        return tag;
    }

    public void reloadEntity() {
        adapterEntity.remove();
        adapterEntity = NMSReflector.mobTypeProvider.createEntityShell(getDefaultWorld(), parentMob.getDataSnapshot());
    }

    public static World getDefaultWorld() {
        World w = Bukkit.getWorlds().get(0);
        if(w == null)
            throw new IllegalStateException("No worlds are loaded?");
        return w;
    }

    public WeakReference<LivingEntity> getAdapterEntity() {
        return new WeakReference<>(adapterEntity);
    }

    public EntityType getEntityType() {
        return adapterEntity.getType();
    }

    public boolean setBaby() {
        if(adapterEntity instanceof Ageable) {
            ((Ageable) adapterEntity).setBaby();
            parentMob.updateTag();
            return true;
        } else {
            return false;
        }
    }

    public boolean setAdult() {
        if(adapterEntity instanceof Ageable) {
            ((Ageable) adapterEntity).setBaby();
            parentMob.updateTag();
            return true;
        } else {
            return false;
        }
    }

    public boolean setHealth(double health) {
        if(health > SpigotConfig.maxHealth) {
            return false;
        }
        adapterEntity.setMaxHealth(health);
        adapterEntity.setHealth(health);
        parentMob.updateTag();
        return true;
    }

    public void setFireTicks(int fireTicks) {
        adapterEntity.setFireTicks(fireTicks);
        parentMob.updateTag();
    }

    public void setCustomName(String name) {
        name = ChatColor.translateAlternateColorCodes('&', name);
        adapterEntity.setCustomNameVisible(true);
        adapterEntity.setCustomName(name);
        parentMob.updateTag();
    }

    public void addPotionEffect(PotionEffect... effects) {
        for (PotionEffect pe : effects) {
            if(pe == null) continue;
            adapterEntity.addPotionEffect(pe);
        }
        parentMob.updateTag();
    }

    public void removePotionEffect(PotionEffectType type) {
        adapterEntity.removePotionEffect(type);
        parentMob.updateTag();
    }

    public void clearPotionEffects() {
        for (PotionEffect pe : adapterEntity.getActivePotionEffects()) {
            adapterEntity.removePotionEffect(pe.getType());
        }
        parentMob.updateTag();
    }

    public double getMaxHealth() {
        return adapterEntity.getMaxHealth();
    }

    public int getFireTicks() {
        return adapterEntity.getFireTicks();
    }

    public String getCustomName() {
        return adapterEntity.getCustomName();
    }

    public Collection<PotionEffect> getAcivePotionEffects() {
        return adapterEntity.getActivePotionEffects();
    }

    public ItemStack getEquipment(EquipmentSlot slot) {
        return slot.getEquipment(adapterEntity.getEquipment());
    }

    public void setEquipment(EquipmentSlot slot, ItemStack item) {
        slot.setEquipment(item, adapterEntity.getEquipment());
        parentMob.updateTag();
    }

}

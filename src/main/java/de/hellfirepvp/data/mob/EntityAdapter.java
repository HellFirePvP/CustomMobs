package de.hellfirepvp.data.mob;

import de.hellfirepvp.api.data.EquipmentSlot;
import de.hellfirepvp.data.nbt.WrappedNBTTagCompound;
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
        this.adapterEntity = NMSReflector.mobTypeProvider.createEntityShell(getWorld(), customMob.getDataSnapshot());
    }

    public WrappedNBTTagCompound getEntityTag() {
        WrappedNBTTagCompound rawTag = NMSReflector.mobTypeProvider.getDataFromEntity(adapterEntity);
        WrappedNBTTagCompound cmobTag = parentMob.getDataAdapter().getPersistentCustomMobsTag();
        rawTag.setSubTag("CustomMobs", cmobTag);
        return rawTag;
    }

    public void reloadEntity() {
        adapterEntity.remove();
        adapterEntity = NMSReflector.mobTypeProvider.createEntityShell(getWorld(), parentMob.getDataSnapshot());
    }

    private static World getWorld() {
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

    public Collection<PotionEffect> getAcivePotionEffects() {
        return adapterEntity.getActivePotionEffects();
    }

    public ItemStack getEquipment(EquipmentSlot slot) {
        EntityEquipment ee = adapterEntity.getEquipment();
        switch (slot) {
            case MAIN_HAND:
                return ee.getItemInMainHand();
            case OFF_HAND:
                return ee.getItemInOffHand();
            case HELMET:
                return ee.getHelmet();
            case CHESTPLATE:
                return ee.getChestplate();
            case LEGGINGS:
                return ee.getLeggings();
            case BOOTS:
                return ee.getBoots();
        }
        return null;
    }

    public void setEquipment(EquipmentSlot slot, ItemStack item) {
        EntityEquipment ee = adapterEntity.getEquipment();
        switch (slot) {
            case MAIN_HAND:
                ee.setItemInMainHand(item);
                break;
            case OFF_HAND:
                ee.setItemInOffHand(item);
                break;
            case BOOTS:
                ee.setBoots(item);
                break;
            case LEGGINGS:
                ee.setLeggings(item);
                break;
            case CHESTPLATE:
                ee.setChestplate(item);
                break;
            case HELMET:
                ee.setHelmet(item);
                break;
        }
        parentMob.updateTag();
    }

}

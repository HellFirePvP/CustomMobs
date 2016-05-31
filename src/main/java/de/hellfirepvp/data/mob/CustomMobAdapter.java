package de.hellfirepvp.data.mob;

import de.hellfirepvp.api.data.EquipmentSlot;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.WatchedNBTEditor;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.api.exception.SpawnLimitException;
import de.hellfirepvp.data.nbt.BufferingNBTEditor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CustomMobAdapter
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:22
 */
public class CustomMobAdapter implements ICustomMob {

    private final CustomMob parent;
    private List<WeakReference<BufferingNBTEditor>> currentlyEditing = new LinkedList<>();

    CustomMobAdapter(CustomMob parent) {
        this.parent = parent;
    }

    @Override
    public WatchedNBTEditor editNBTTag() {
        BufferingNBTEditor editor = new BufferingNBTEditor(parent, parent.getDataSnapshot());
        currentlyEditing.add(new WeakReference<>(editor));
        return editor;
    }

    protected final void invalidateAPIs() {
        for (WeakReference<BufferingNBTEditor> ref : currentlyEditing) {
            if(ref.get() == null) continue;
            try {
                ref.get().invalidate();
            } catch (NullPointerException ignored) {}
        }
        currentlyEditing.clear();
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

    @Override
    public Double getMaxHealth() {
        return parent.getEntityAdapter().getMaxHealth();
    }

    @Override
    public Integer getBurnTime() {
        return parent.getEntityAdapter().getFireTicks();
    }

    @Override
    public Integer getExperienceDrop() {
        return parent.getDataAdapter().getExperienceDrop();
    }

    @Override
    public String getDisplayName() {
        return parent.getEntityAdapter().getCustomName();
    }

    @Override
    public String getCommandLine() {
        return parent.getDataAdapter().getCommandToExecute();
    }

    @Override
    public Integer getSpawnLimit() {
        return parent.getDataAdapter().getSpawnLimit();
    }

    @Override
    public Boolean isFireProof() {
        return parent.getDataAdapter().isFireProof();
    }

    @Override
    public Collection<PotionEffect> getPotionEffects() {
        return parent.getEntityAdapter().getAcivePotionEffects();
    }

    @Override
    public Map<EquipmentSlot, ItemStack> getEquipment() {
        Map<EquipmentSlot, ItemStack> eq = new HashMap<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack item = parent.getEntityAdapter().getEquipment(slot);
            if(item != null) {
                eq.put(slot, item);
            }
        }
        return eq;
    }

    @Override
    public Map<ItemStack, Double> getDrops() {
        return Collections.unmodifiableMap(parent.getDataAdapter().getItemDrops());
    }

}

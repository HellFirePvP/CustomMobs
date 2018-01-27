package de.hellfirepvp.data.mob;

import de.hellfirepvp.api.data.WatchedNBTEditor;
import java.util.Collections;
import java.util.HashMap;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.api.data.EquipmentSlot;
import java.util.Map;
import org.bukkit.potion.PotionEffect;
import java.util.Collection;
import de.hellfirepvp.api.exception.SpawnLimitException;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import java.util.Iterator;
import java.util.LinkedList;
import de.hellfirepvp.data.nbt.BufferingNBTEditor;
import java.lang.ref.WeakReference;
import java.util.List;
import de.hellfirepvp.api.data.ICustomMob;

public class CustomMobAdapter implements ICustomMob
{
    private final CustomMob parent;
    private List<WeakReference<BufferingNBTEditor>> currentlyEditing;
    
    CustomMobAdapter(final CustomMob parent) {
        this.currentlyEditing = new LinkedList<WeakReference<BufferingNBTEditor>>();
        this.parent = parent;
    }
    
    @Override
    public BufferingNBTEditor editNBTTag() {
        final BufferingNBTEditor editor = new BufferingNBTEditor(this.parent, this.parent.getDataSnapshot());
        this.currentlyEditing.add(new WeakReference<BufferingNBTEditor>(editor));
        return editor;
    }
    
    protected final void invalidateAPIs() {
        for (final WeakReference<BufferingNBTEditor> ref : this.currentlyEditing) {
            if (ref.get() == null) {
                continue;
            }
            try {
                ref.get().invalidate();
            }
            catch (NullPointerException ex) {}
        }
        this.currentlyEditing.clear();
    }
    
    public final CustomMob getParent() {
        return this.parent;
    }
    
    @Override
    public WrappedNBTTagCompound getReadOnlyTag() {
        return this.parent.getDataSnapshot().unmodifiable();
    }
    
    @Override
    public String getName() {
        return this.parent.getMobFileName();
    }
    
    @Override
    public EntityType getEntityType() {
        return this.parent.getEntityAdapter().getEntityType();
    }
    
    @Override
    public LivingEntity spawnAt(final Location at) throws SpawnLimitException {
        return this.parent.spawnAt(at);
    }
    
    @Override
    public Double getMaxHealth() {
        return this.parent.getEntityAdapter().getMaxHealth();
    }
    
    @Override
    public Integer getBurnTime() {
        return this.parent.getEntityAdapter().getFireTicks();
    }
    
    @Deprecated
    @Override
    public Integer getExperienceDrop() {
        return this.parent.getDataAdapter().getExpDropLower();
    }
    
    @Override
    public Integer getLowestExperienceDrop() {
        return this.parent.getDataAdapter().getExpDropLower();
    }
    
    @Override
    public Integer getHighestExperienceDrop() {
        return this.parent.getDataAdapter().getExpDropHigher();
    }
    
    @Override
    public String getDisplayName() {
        return this.parent.getEntityAdapter().getCustomName();
    }
    
    @Override
    public String getCommandLine() {
        return this.parent.getDataAdapter().getCommandToExecute();
    }
    
    @Override
    public Integer getSpawnLimit() {
        return this.parent.getDataAdapter().getSpawnLimit();
    }
    
    @Override
    public Boolean isFireProof() {
        return this.parent.getDataAdapter().isFireProof();
    }
    
    @Override
    public Collection<PotionEffect> getPotionEffects() {
        return this.parent.getEntityAdapter().getAcivePotionEffects();
    }
    
    @Override
    public Map<EquipmentSlot, ItemStack> getEquipment() {
        final Map<EquipmentSlot, ItemStack> eq = new HashMap<EquipmentSlot, ItemStack>();
        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            final ItemStack item = this.parent.getEntityAdapter().getEquipment(slot);
            if (item != null) {
                eq.put(slot, item);
            }
        }
        return eq;
    }
    
    @Override
    public List<ItemDrop> getDrops() {
        return Collections.unmodifiableList((List<? extends ItemDrop>)this.parent.getDataAdapter().getItemDrops());
    }
}

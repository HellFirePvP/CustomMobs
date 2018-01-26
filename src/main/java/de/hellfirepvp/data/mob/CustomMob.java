package de.hellfirepvp.data.mob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.nbt.UnsupportedNBTTypeException;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.data.nbt.BufferingNBTEditor;
import de.hellfirepvp.file.write.MobDataWriter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.api.exception.SpawnLimitException;
import de.hellfirepvp.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CustomMob
 * Created by HellFirePvP
 * Date: 24.05.2016 / 16:27
 */
public class CustomMob {

    private static final Map<CustomMob, List<LivingEntity>> alive = new HashMap<>();

    private final String name;
    protected WrappedNBTTagCompound snapshotTag;
    private EntityAdapter entityAdapter;
    private DataAdapter dataAdapter;
    private List<WeakReference<CustomMobAdapter>> activeAdapters = new LinkedList<>();

    public CustomMob(String name, WrappedNBTTagCompound data) {
        this.name = name;
        this.snapshotTag = data;
        this.entityAdapter = new EntityAdapter(this);
        this.dataAdapter = new DataAdapter(this);
    }

    public EntityAdapter getEntityAdapter() {
        return entityAdapter;
    }

    public DataAdapter getDataAdapter() {
        return dataAdapter;
    }

    public CustomMobAdapter createApiAdapter() {
        CustomMobAdapter apiAdapter = new CustomMobAdapter(this);
        activeAdapters.add(new WeakReference<>(apiAdapter));
        return apiAdapter;
    }

    protected final void updateTag() {
        this.snapshotTag = entityAdapter.getEntityTag();
        writeTag();
    }

    private void invalidateAPIs() {
        for (WeakReference<CustomMobAdapter> ref : activeAdapters) {
            if(ref.get() == null) continue;
            try {
                ref.get().invalidateAPIs();
            } catch (NullPointerException exc) {
                CustomMobs.logger.warning("Invalidating API accesses for " + name + " failed. Skipping...");
            }
        }
        activeAdapters.clear();
    }

    public void askForSave(BufferingNBTEditor bufferingNBTEditor) {
        try {
            this.snapshotTag = entityAdapter.getEntityTag();
        } catch (Exception exc) {
            CustomMobs.logger.warning("Could not update entityTag for " + name + " - did somebody mess with the NBT tag?");
            if(CustomMobs.instance.getConfigHandler().debug()) {
                CustomMobs.logger.warning("Debug is enabled - StackTrace:");
                exc.printStackTrace();
            }
        }
        CustomMobs.logger.debug("API asked for MobFile saving for CustomMob " + name);
        bufferingNBTEditor.executeQueriesOn(this.snapshotTag);
        writeTag();
    }

    public void askForSave(BufferingNBTEditor editor, CommandSender cs) {
        try {
            this.snapshotTag = entityAdapter.getEntityTag();
        } catch (Exception exc) {
            CustomMobs.logger.warning("Could not update entityTag for " + name + " - did somebody mess with the NBT tag?");
            if(CustomMobs.instance.getConfigHandler().debug()) {
                CustomMobs.logger.warning("Debug is enabled - StackTrace:");
                exc.printStackTrace();
            }
        }
        CustomMobs.logger.debug("API asked for MobFile saving for CustomMob " + name);
        editor.executeQueriesOn(this.snapshotTag);

        MobDataWriter.writeMobFile(this);
        invalidateAPIs();
        entityAdapter.reloadEntity();
        try {
            this.snapshotTag = entityAdapter.getEntityTag();
        } catch (Exception exc) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                    String.format(LanguageHandler.translate("command.cnbt.setraw.entity.error"), getMobFileName()));
            CustomMobs.logger.warning("Could not update entityTag for " + name + " - did somebody mess with the NBT tag?");
            if(CustomMobs.instance.getConfigHandler().debug()) {
                CustomMobs.logger.warning("Debug is enabled - StackTrace:");
                exc.printStackTrace();
            }
        }
    }

    protected final boolean saveTagAlongWith(String entry, Object value) {
        WrappedNBTTagCompound tag = entityAdapter.getEntityTag();
        try {
            tag.set(entry, value);
        } catch (UnsupportedNBTTypeException e) {
            return false;
        }
        this.snapshotTag = tag;
        writeTag();
        return true;
    }

    private void writeTag() {
        MobDataWriter.writeMobFile(this);
        invalidateAPIs();
        entityAdapter.reloadEntity();
    }

    public String getMobFileName() {
        return name;
    }

    public WrappedNBTTagCompound getDataSnapshot() {
        return snapshotTag;
    }

    public LivingEntity spawnAt(Location l) throws SpawnLimitException {
        if(!CustomMobs.instance.getSpawnLimiter().canSpawn(getMobFileName())) throw new SpawnLimitException("SpawnLimit denies spawning of " + name);
        LivingEntity entity = unsafe_Spawn(l);
        if(entity == null)
            throw new IllegalStateException("Unknown EntityType for " + getMobFileName() + " or NBTTag is missing some information or contains wrong data!");
        CustomMobs.instance.getSpawnLimiter().spawnedIncrement(getMobFileName(), entity);
        if(!alive.containsKey(this)) alive.put(this, new LinkedList<>());

        alive.get(this).add(entity);

        CustomMob riding = CustomMobs.instance.getStackingData().getMountingMob(this);
        if(riding != null) {
            LivingEntity ridingEntity = riding.spawnAt(l);
            entity.setPassenger(ridingEntity);
        }
        return entity;
    }

    private LivingEntity unsafe_Spawn(Location l) {
        LivingEntity le = NMSReflector.mobTypeProvider.spawnEntityFromSerializedData(l, snapshotTag);
        if(le == null) return null;
        EntityEquipment ee = le.getEquipment();
        ee.setBootsDropChance(0F);
        ee.setLeggingsDropChance(0F);
        ee.setChestplateDropChance(0F);
        ee.setHelmetDropChance(0F);
        ee.setItemInMainHandDropChance(0F);
        ee.setItemInOffHandDropChance(0F);

        le.setRemoveWhenFarAway(false);
        le.setCanPickupItems(false);

        if(dataAdapter.isFireProof()) {
            EntityUtils.setFireproof(le);
        }
        NMSReflector.nmsUtils.setName(le, getMobFileName());

        if(LeashManager.shouldBeLeashed(this)) {
            LeashManager.leash(le, this);
        }

        return le;
    }

    //
    // Registry removal methods.
    //

    public static void kill(CustomMob mob, LivingEntity entity) {
        List<LivingEntity> entities = alive.get(mob);
        if(entities != null) {
            entities.remove(entity);
        }
        if(mob.dataAdapter.getSpawnLimit() > 0) {
            CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), entity);
        }
    }

    public static int killAll(String mobName) {
        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
        if(mob != null) return killAll(mob);
        return 0;
    }

    public static int killAll(CustomMob mob) {
        int count = 0;
        List<LivingEntity> aliveInstances = alive.get(mob);
        if(aliveInstances != null) {
            for(LivingEntity entity : aliveInstances) {
                count++;
                entity.remove();
                if(mob.dataAdapter.getSpawnLimit() > 0) {
                    CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), entity);
                }
            }
            alive.get(mob).clear();
        }
        return count;
    }

    public static void killAll() {
        alive.keySet().forEach(de.hellfirepvp.data.mob.CustomMob::killAll);
        alive.clear();
    }

    public static int killAllWithLimit() {
        int count = 0;
        for(CustomMob mob : alive.keySet()) {
            if(mob.dataAdapter.getSpawnLimit() > 0) {
                count += killAll(mob);
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomMob customMob = (CustomMob) o;
        return !(name != null ? !name.equals(customMob.name) : customMob.name != null);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public static class DropInventoryWrapper implements InventoryHolder {

        private final CustomMob mob;
        private final Inventory inventory;
        private final int pageIndex;
        private final boolean hasPrev, hasNext;

        public DropInventoryWrapper(CustomMob mob, int dropPageIndex) {
            int drops = mob.getDataAdapter().getItemDrops().size();
            int dropPagesNeeded = (drops / 36) + 1;
            if(drops % 36 == 0 && drops > 0)
                dropPagesNeeded--;

            if(dropPageIndex < 0)
                dropPageIndex = 0;

            while (dropPageIndex >= dropPagesNeeded)
                dropPageIndex--;

            this.inventory = Bukkit.createInventory(this, 54, String.format(LanguageHandler.translate("command.cmob.drop.gui.name"), mob.getMobFileName(), String.valueOf(dropPageIndex)));
            this.hasPrev = dropPageIndex > 0;
            this.hasNext = (dropPageIndex + 1) < dropPagesNeeded;
            this.pageIndex = dropPageIndex;
            this.mob = mob;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public boolean hasPrev() {
            return hasPrev;
        }

        public CustomMob getMob() {
            return mob;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }

}

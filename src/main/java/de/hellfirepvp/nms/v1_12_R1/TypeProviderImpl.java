package de.hellfirepvp.nms.v1_12_R1;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import de.hellfirepvp.nms.NMSReflector;
import net.minecraft.server.v1_12_R1.DragonControllerPhase;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.entity.CreatureSpawnEvent;
import java.util.UUID;
import org.bukkit.Location;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import net.minecraft.server.v1_12_R1.WorldServer;
import net.minecraft.server.v1_12_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import java.util.Collections;
import java.util.LinkedList;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import de.hellfirepvp.CustomMobs;
import java.lang.reflect.Modifier;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import java.util.List;
import net.minecraft.server.v1_12_R1.EntityTypes;
import java.util.HashMap;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
import net.minecraft.server.v1_12_R1.Entity;
import java.util.Map;
import de.hellfirepvp.nms.RegistryTypeProvider;

public class TypeProviderImpl implements RegistryTypeProvider
{
    private Map<String, Class<? extends Entity>> discoveredTypes;
    private RegistryMaterials<MinecraftKey, Class<? extends Entity>> registryTypes;
    
    public TypeProviderImpl() {
        this.discoveredTypes = new HashMap<String, Class<? extends Entity>>();
        this.registryTypes = (RegistryMaterials<MinecraftKey, Class<? extends Entity>>)new RegistryMaterials();
    }
    
    @Override
    public void discoverMobTypes() {
        this.discoveredTypes.clear();
        try {
            final Field namesListField = EntityTypes.class.getDeclaredField("g");
            namesListField.setAccessible(true);
            final List<String> nameList = (List<String>)namesListField.get(null);
            for (final String name : nameList) {
                final int index = nameList.indexOf(name);
                final Class<? extends Entity> entityClass = (Class<? extends Entity>)EntityTypes.b.getId(index);
                if (entityClass != null && EntityInsentient.class.isAssignableFrom(entityClass) && !Modifier.isAbstract(entityClass.getModifiers())) {
                    this.discoveredTypes.put(name, entityClass);
                    CustomMobs.logger.debug("Discovered mobType: " + name);
                }
            }
            final Field registryField = EntityTypes.class.getDeclaredField("b");
            registryField.setAccessible(true);
            final RegistryMaterials<MinecraftKey, Class<? extends Entity>> registry = (RegistryMaterials<MinecraftKey, Class<? extends Entity>>)registryField.get(null);
            CustomMobs.logger.debug("Discovered Entity Registry");
            for (final MinecraftKey key : registry.keySet()) {
                CustomMobs.logger.debug("Discovered registry entry: " + key.toString());
            }
            CustomMobs.logger.debug("Setting delegate to registry");
            this.registryTypes = registry;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Nullable
    private Class<? extends Entity> getEntityClass(final String name) {
        return this.discoveredTypes.get(name);
    }
    
    @Override
    public List<String> getTypeNames() {
        return Collections.unmodifiableList((List<? extends String>)new LinkedList<String>(this.discoveredTypes.keySet()));
    }
    
    @Override
    public boolean doesMobTypeExist(final String name) {
        return EntityTypes.b(new MinecraftKey(name));
    }
    
    @Nullable
    @Override
    public String tryTranslateNameToRegistry(final String name) {
        try {
            return EntityTypes.getName((Class)this.getEntityClass(name)).toString();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    @Nullable
    @Override
    public String tryTranslateRegistryNameToName(final String regName) {
        return EntityTypes.a(new MinecraftKey(regName));
    }
    
    @Override
    public LivingEntity getEntityForName(final World world, final String typeName) {
        final WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        final Entity e = EntityTypes.a((Class)this.getEntityClass(typeName), (net.minecraft.server.v1_12_R1.World)nmsWorld);
        if (e != null && EntityLiving.class.isAssignableFrom(e.getClass())) {
            return (LivingEntity)e.getBukkitEntity();
        }
        CustomMobs.logger.info("Skipping invalid entity creation....");
        return null;
    }
    
    @Override
    public LivingEntity createEntityShell(final World world, final WrappedNBTTagCompound data) {
        final WorldServer ws = ((CraftWorld)world).getHandle();
        final Entity e = EntityTypes.a((NBTTagCompound)data.getRawNMSTagCompound(), (net.minecraft.server.v1_12_R1.World)ws);
        if (e != null && EntityLiving.class.isAssignableFrom(e.getClass())) {
            return (LivingEntity)e.getBukkitEntity();
        }
        CustomMobs.logger.info("Skipping invalid entity creation...");
        return null;
    }
    
    @Override
    public LivingEntity spawnEntityFromSerializedData(final Location at, final WrappedNBTTagCompound data) {
        final World world = at.getWorld();
        final WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        final Entity e = EntityTypes.a((NBTTagCompound)data.getRawNMSTagCompound(), (net.minecraft.server.v1_12_R1.World)nmsWorld);
        if (e != null && EntityLiving.class.isAssignableFrom(e.getClass())) {
            e.setPosition(at.getX(), at.getY(), at.getZ());
            e.motX = 0.0;
            e.motY = 0.0;
            e.motZ = 0.0;
            e.dimension = nmsWorld.dimension;
            e.a(UUID.randomUUID());
            nmsWorld.addEntity(e, CreatureSpawnEvent.SpawnReason.CUSTOM);
            final LivingEntity le = (LivingEntity)e.getBukkitEntity();
            if (le instanceof EnderDragon) {
                final CraftEnderDragon ced = (CraftEnderDragon)le;
                ced.getHandle().getDragonControllerManager().setControllerPhase(DragonControllerPhase.a);
            }
            return le;
        }
        CustomMobs.logger.info("Skipping invalid entity spawning....");
        return null;
    }
    
    @Override
    public WrappedNBTTagCompound getDataFromEntity(final LivingEntity le) {
        if (NMSReflector.nbtProvider == null || le == null) {
            return null;
        }
        final EntityLiving e = ((CraftLivingEntity)le).getHandle();
        final WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
        e.c((NBTTagCompound)tag.getRawNMSTagCompound());
        return tag;
    }
    
    @Override
    public void mergeEntityDataInto(final LivingEntity le, final WrappedNBTTagCompound tag) {
        if (NMSReflector.nbtProvider == null || le == null) {
            return;
        }
        final EntityLiving e = ((CraftLivingEntity)le).getHandle();
        e.c((NBTTagCompound)tag.getRawNMSTagCompound());
    }
}

package de.hellfirepvp.nms.v1_9_R1;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import de.hellfirepvp.nms.NMSReflector;
import net.minecraft.server.v1_9_R1.DragonControllerPhase;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.entity.CreatureSpawnEvent;
import java.util.UUID;
import org.bukkit.Location;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.WorldServer;
import net.minecraft.server.v1_9_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import java.util.Collections;
import java.util.Iterator;
import java.lang.reflect.Field;
import de.hellfirepvp.CustomMobs;
import java.lang.reflect.Modifier;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import java.util.Map;
import net.minecraft.server.v1_9_R1.EntityTypes;
import java.util.LinkedList;
import java.util.List;
import de.hellfirepvp.nms.MobTypeProvider;

public class TypeProviderImpl implements MobTypeProvider
{
    private List<String> discoveredTypes;
    
    public TypeProviderImpl() {
        this.discoveredTypes = new LinkedList<String>();
    }
    
    @Override
    public void discoverMobTypes() {
        this.discoveredTypes.clear();
        try {
            final Field nameToClassField = EntityTypes.class.getDeclaredField("c");
            nameToClassField.setAccessible(true);
            final Map<String, Class> nameToClassMap = (Map<String, Class>)nameToClassField.get(null);
            for (final String s : nameToClassMap.keySet()) {
                final Class entityClass = nameToClassMap.get(s);
                if (EntityInsentient.class.isAssignableFrom(entityClass) && !Modifier.isAbstract(entityClass.getModifiers())) {
                    this.discoveredTypes.add(s);
                    CustomMobs.logger.debug("Discovered mobType: " + s);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<String> getTypeNames() {
        return Collections.unmodifiableList((List<? extends String>)this.discoveredTypes);
    }
    
    @Override
    public LivingEntity getEntityForName(final World world, final String typeName) {
        final WorldServer nmsWorld = ((CraftWorld)world).getHandle();
        final Entity e = EntityTypes.createEntityByName(typeName, (net.minecraft.server.v1_9_R1.World)nmsWorld);
        if (e != null && EntityLiving.class.isAssignableFrom(e.getClass())) {
            return (LivingEntity)e.getBukkitEntity();
        }
        CustomMobs.logger.info("Skipping invalid entity creation....");
        return null;
    }
    
    @Override
    public LivingEntity createEntityShell(final World world, final WrappedNBTTagCompound data) {
        final WorldServer ws = ((CraftWorld)world).getHandle();
        final Entity e = EntityTypes.a((NBTTagCompound)data.getRawNMSTagCompound(), (net.minecraft.server.v1_9_R1.World)ws);
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
        final Entity e = EntityTypes.a((NBTTagCompound)data.getRawNMSTagCompound(), (net.minecraft.server.v1_9_R1.World)nmsWorld);
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
                ced.getHandle().cT().a(DragonControllerPhase.a);
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

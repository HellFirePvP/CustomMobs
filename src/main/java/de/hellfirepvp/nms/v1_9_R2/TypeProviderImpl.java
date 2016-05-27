package de.hellfirepvp.nms.v1_9_R2;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.nms.MobTypeProvider;
import de.hellfirepvp.nms.NMSReflector;
import net.minecraft.server.v1_9_R2.Entity;
import net.minecraft.server.v1_9_R2.EntityLiving;
import net.minecraft.server.v1_9_R2.EntityTypes;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: TypeProviderImpl
 * Created by HellFirePvP
 * Date: 24.05.2016 / 14:40
 */
public class TypeProviderImpl implements MobTypeProvider {

    private List<String> discoveredTypes = new LinkedList<>();

    @Override
    public void discoverMobTypes() {
        discoveredTypes.clear();
        try {
            Field nameToClassField = EntityTypes.class.getDeclaredField("c");
            nameToClassField.setAccessible(true);
            Map<String, Class> nameToClassMap = (Map<String, Class>) nameToClassField.get(null);
            for (String s : nameToClassMap.keySet()) {
                Class entityClass = nameToClassMap.get(s);
                if(EntityLiving.class.isAssignableFrom(entityClass) &&
                        !Modifier.isAbstract(entityClass.getModifiers())) {
                    discoveredTypes.add(s);

                    CustomMobs.logger.info("Discovered mobType: " + s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getTypeNames() {
        return Collections.unmodifiableList(discoveredTypes);
    }

    @Override
    public LivingEntity getEntityForName(World world, String typeName) {
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        Entity e = EntityTypes.createEntityByName(typeName, nmsWorld);
        if(e != null && EntityLiving.class.isAssignableFrom(e.getClass())) { //.isALivingEntity check.. kinda..
            return (LivingEntity) e.getBukkitEntity();
        } else {
            CustomMobs.logger.info("Skipping invalid entity creation....");
            return null;
        }
    }

    @Override
    public LivingEntity createEntityShell(World world, WrappedNBTTagCompound data) {
        WorldServer ws = ((CraftWorld) world).getHandle();
        Entity e = EntityTypes.a((NBTTagCompound) data.getRawNMSTagCompound(), ws);
        if(e != null && EntityLiving.class.isAssignableFrom(e.getClass())) { //.isALivingEntity check.. kinda..
            return (LivingEntity) e.getBukkitEntity();
        } else {
            CustomMobs.logger.info("Skipping invalid entity creation...");
            return null;
        }
    }

    @Override
    public LivingEntity spawnEntityFromSerializedData(Location at, WrappedNBTTagCompound data) {
        World world = at.getWorld();
        WorldServer nmsWorld = ((CraftWorld) world).getHandle();
        Entity e = EntityTypes.a((NBTTagCompound) data.getRawNMSTagCompound(), nmsWorld);
        if(e != null && EntityLiving.class.isAssignableFrom(e.getClass())) { //.isALivingEntity check.. kinda..
            e.setPosition(at.getX(), at.getY(), at.getZ());
            e.motX = 0;
            e.motY = 0;
            e.motZ = 0;
            e.dimension = nmsWorld.dimension;
            e.a(UUID.randomUUID());
            nmsWorld.addEntity(e, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } else {
            CustomMobs.logger.info("Skipping invalid entity spawning....");
            return null;
        }
        return (LivingEntity) e.getBukkitEntity();
    }

    @Override
    public WrappedNBTTagCompound getDataFromEntity(LivingEntity le) {
        if(NMSReflector.nbtProvider == null || le == null) return null;
        EntityLiving e = ((CraftLivingEntity) le).getHandle();
        WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
        e.c((NBTTagCompound) tag.getRawNMSTagCompound());
        return tag;
    }


}

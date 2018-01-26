package de.hellfirepvp.nms.v1_12_R1;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.nms.MobTypeProvider;
import de.hellfirepvp.nms.NMSReflector;
import net.minecraft.server.v1_12_R1.DragonControllerPhase;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

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
 * Created by HellFirePvP, updated by VitorMac10
 * Date: 26.01.2018 / 11:23
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
                if(EntityInsentient.class.isAssignableFrom(entityClass) &&
                        !Modifier.isAbstract(entityClass.getModifiers())) {
                    discoveredTypes.add(s);

                    CustomMobs.logger.debug("Discovered mobType: " + s);
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
        Entity e = EntityTypes.a(EntityTypes.b.get(new net.minecraft.server.v1_12_R1.MinecraftKey(typeName)), nmsWorld);
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
        LivingEntity le = (LivingEntity) e.getBukkitEntity();
        if(le instanceof EnderDragon) {
            CraftEnderDragon ced = (CraftEnderDragon) le;
            ced.getHandle().getDragonControllerManager().setControllerPhase(DragonControllerPhase.a);
        }
        return le;
    }

    @Override
    public WrappedNBTTagCompound getDataFromEntity(LivingEntity le) {
        if(NMSReflector.nbtProvider == null || le == null) return null;
        EntityLiving e = ((CraftLivingEntity) le).getHandle();
        WrappedNBTTagCompound tag = NMSReflector.nbtProvider.newTagCompound();
        e.c((NBTTagCompound) tag.getRawNMSTagCompound());
        return tag;
    }

    @Override
    public void mergeEntityDataInto(LivingEntity le, WrappedNBTTagCompound tag) {
        if(NMSReflector.nbtProvider == null || le == null) return;
        EntityLiving e = ((CraftLivingEntity) le).getHandle();
        e.c((NBTTagCompound) tag.getRawNMSTagCompound());
    }
}

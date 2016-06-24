package de.hellfirepvp.nms.v1_9_R1;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.nms.NMSUtils;
import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.Blocks;
import net.minecraft.server.v1_9_R1.Chunk;
import net.minecraft.server.v1_9_R1.ChunkProviderServer;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityLiving;
import net.minecraft.server.v1_9_R1.EnumCreatureType;
import net.minecraft.server.v1_9_R1.EnumItemSlot;
import net.minecraft.server.v1_9_R1.IBlockData;
import net.minecraft.server.v1_9_R1.Item;
import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NMSUtilImpl
 * Created by HellFirePvP
 * Date: 30.05.2016 / 08:53
 */
public class NMSUtilImpl implements NMSUtils {

    @Override
    public void setName(LivingEntity entity, String name) {
        EntityLiving living = ((CraftLivingEntity) entity).getHandle();
        try {
            ItemStack stack = living.getEquipment(EnumItemSlot.LEGS);
            if(stack == null) {
                stack = new ItemStack(Item.getItemOf(Blocks.STONE));
            }
            stack.c(name);
            living.setSlot(EnumItemSlot.LEGS, stack);
        } catch (Exception exc) {
            CustomMobs.logger.severe("Saving name in NBT failed. Cause: " + exc.getMessage());
        }
    }

    @Override
    public String getName(LivingEntity entity) {
        EntityLiving living = ((CraftLivingEntity) entity).getHandle();
        try {
            ItemStack stack = living.getEquipment(EnumItemSlot.LEGS);
            if(stack == null) return null;
            if(!stack.hasName()) return null;
            return stack.getName();
        } catch (Exception ignored) {}
        return null;
    }

    private static Field spigotCfgField;
    private static boolean isSpigot = true;

    @Override
    public byte[] getMobSpawnRangeAndViewDistance(World world) {
        net.minecraft.server.v1_9_R1.WorldServer ws = ((CraftWorld) world).getHandle();
        if(spigotCfgField == null && isSpigot) {
            try {
                spigotCfgField = net.minecraft.server.v1_9_R1.World.class.getDeclaredField("spigotConfig");
            } catch (NoSuchFieldException e) {
                isSpigot = false;
                return getMobSpawnRangeAndViewDistance(world);
            }
        }
        byte[] data = new byte[2];
        byte viewDistance, mobSpawnRange;
        if(isSpigot && spigotCfgField != null) {
            spigotCfgField.setAccessible(true);
            try {
                Object spigotCfg = spigotCfgField.get(ws);
                int viewDst = (int) spigotCfg.getClass().getDeclaredField("viewDistance").get(spigotCfg);
                viewDistance = (byte) viewDst;
                mobSpawnRange = (byte) spigotCfg.getClass().getDeclaredField("mobSpawnRange").get(spigotCfg);
            } catch (Exception e) {
                isSpigot = false;
                return getMobSpawnRangeAndViewDistance(world);
            }
        } else {
            viewDistance = 8;
            mobSpawnRange = 8;
        }
        data[0] = mobSpawnRange;
        data[1] = viewDistance;
        return data;
    }

    private static Field entityCountField;

    static {

        try {
            entityCountField = Chunk.class.getDeclaredField("entityCount");
            entityCountField.setAccessible(true);
        } catch (Exception e) {}

    }

    @Override
    public int getLivingCount(WorldSpawner.CreatureType creatureType, List<WorldSpawner.ChunkCoordPair> chCoords, World world) {
        EnumCreatureType type;
        switch (creatureType) {
            case AMBIENT:
                type = EnumCreatureType.AMBIENT;
                break;
            case WATER_CREATURE:
                type = EnumCreatureType.WATER_CREATURE;
                break;
            case ANIMAL:
                type = EnumCreatureType.CREATURE;
                break;
            case MONSTER:
                type = EnumCreatureType.MONSTER;
                break;
            default:
                return 0;
        }
        Class<?> clazz = type.a();
        WorldServer ws = ((CraftWorld) world).getHandle();
        int cnt = 0;
        for (WorldSpawner.ChunkCoordPair p : chCoords) {
            if (ws.getChunkProviderServer().isChunkLoaded(p.x, p.z)) {
                try {
                    cnt += ((TObjectIntHashMap<Class>) entityCountField.get(ws.getChunkAt(p.x, p.z))).get(clazz);
                } catch (Exception e) {}
            }
        }
        return cnt;
    }

    @Override
    public boolean isPlayerInRange(Location location, double range) {
        return ((CraftWorld) location.getWorld()).getHandle().a(location.getX(), location.getY(), location.getZ(), range, false) != null;
    }

    @Override
    public void clearSpawner(Block block) {
        org.bukkit.block.BlockState state = block.getState();
        if(state == null || !(block instanceof CreatureSpawner)) {
            return;
        }
        try {
            CraftCreatureSpawner spawner = (CraftCreatureSpawner) state;
            spawner.getTileEntity().getSpawner().setMobName("");
        } catch (Exception ignored) {}
    }

    @Override
    public boolean isNormalCube(World world, Vector at) {
        WorldServer ws = ((CraftWorld) world).getHandle();
        IBlockData state = ws.getType(new BlockPosition(at.getBlockX(), at.getBlockY(), at.getBlockZ()));
        return state.l();
    }

}

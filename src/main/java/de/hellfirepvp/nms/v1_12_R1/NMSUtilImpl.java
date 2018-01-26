package de.hellfirepvp.nms.v1_12_R1;

import net.minecraft.server.v1_12_R1.Chunk;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.util.Vector;
import org.bukkit.block.BlockState;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftCreatureSpawner;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Block;
import org.bukkit.Location;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.server.v1_12_R1.EnumCreatureType;
import java.util.List;
import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.World;
import net.minecraft.server.v1_12_R1.EntityLiving;
import de.hellfirepvp.CustomMobs;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import java.lang.reflect.Field;
import de.hellfirepvp.nms.NMSUtils;
import org.bukkit.craftbukkit.v1_12_R1.block.Reflector;

public class NMSUtilImpl implements NMSUtils
{
    private static Field spigotCfgField;
    private static boolean isSpigot;
    private static Field entityCountField;
    
    @Override
    public void setName(final LivingEntity entity, final String name) {
        final EntityLiving living = ((CraftLivingEntity)entity).getHandle();
        try {
            ItemStack stack = living.getEquipment(EnumItemSlot.LEGS);
            if (stack.isEmpty()) {
                stack = new ItemStack(Item.getItemOf(Blocks.STONE));
            }
            stack.g(name);
            living.setSlot(EnumItemSlot.LEGS, stack);
        }
        catch (Exception exc) {
            CustomMobs.logger.severe("Saving name in NBT failed. Cause: " + exc.getMessage());
        }
    }
    
    @Override
    public String getName(final LivingEntity entity) {
        final EntityLiving living = ((CraftLivingEntity)entity).getHandle();
        try {
            final ItemStack stack = living.getEquipment(EnumItemSlot.LEGS);
            if (stack.isEmpty()) {
                return null;
            }
            if (!stack.hasName()) {
                return null;
            }
            return stack.getName();
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    public byte[] getMobSpawnRangeAndViewDistance(final World world) {
        final WorldServer ws = ((CraftWorld)world).getHandle();
        if (NMSUtilImpl.spigotCfgField == null && NMSUtilImpl.isSpigot) {
            try {
                NMSUtilImpl.spigotCfgField = net.minecraft.server.v1_12_R1.World.class.getDeclaredField("spigotConfig");
            }
            catch (NoSuchFieldException e) {
                NMSUtilImpl.isSpigot = false;
                return this.getMobSpawnRangeAndViewDistance(world);
            }
        }
        final byte[] data = new byte[2];
        byte viewDistance = 0;
        byte mobSpawnRange = 0;
        Label_0150: {
            if (NMSUtilImpl.isSpigot && NMSUtilImpl.spigotCfgField != null) {
                NMSUtilImpl.spigotCfgField.setAccessible(true);
                try {
                    final Object spigotCfg = NMSUtilImpl.spigotCfgField.get(ws);
                    final int viewDst = (int)spigotCfg.getClass().getDeclaredField("viewDistance").get(spigotCfg);
                    viewDistance = (byte)viewDst;
                    mobSpawnRange = (byte)spigotCfg.getClass().getDeclaredField("mobSpawnRange").get(spigotCfg);
                    break Label_0150;
                }
                catch (Exception e2) {
                    NMSUtilImpl.isSpigot = false;
                    return this.getMobSpawnRangeAndViewDistance(world);
                }
            }
            viewDistance = 8;
            mobSpawnRange = 8;
        }
        data[0] = mobSpawnRange;
        data[1] = viewDistance;
        return data;
    }
    
    @Override
    public int getLivingCount(final WorldSpawner.CreatureType creatureType, final List<WorldSpawner.ChunkCoordPair> chCoords, final World world) {
        EnumCreatureType type = null;
        switch (creatureType) {
            case AMBIENT: {
                type = EnumCreatureType.AMBIENT;
                break;
            }
            case WATER_CREATURE: {
                type = EnumCreatureType.WATER_CREATURE;
                break;
            }
            case ANIMAL: {
                type = EnumCreatureType.CREATURE;
                break;
            }
            case MONSTER: {
                type = EnumCreatureType.MONSTER;
                break;
            }
            default: {
                return 0;
            }
        }
        final Class<?> clazz = (Class<?>)type.a();
        final WorldServer ws = ((CraftWorld)world).getHandle();
        int cnt = 0;
        for (final WorldSpawner.ChunkCoordPair p : chCoords) {
            if (ws.getChunkProviderServer().isLoaded(p.x, p.z)) {
                try {
                    cnt += ((TObjectIntHashMap)NMSUtilImpl.entityCountField.get(ws.getChunkAt(p.x, p.z))).get((Object)clazz);
                }
                catch (Exception ex) {}
            }
        }
        return cnt;
    }
    
    @Override
    public boolean isPlayerInRange(final Location location, final double range) {
        return ((CraftWorld)location.getWorld()).getHandle().a(location.getX(), location.getY(), location.getZ(), range, false) != null;
    }
    
    @Override
    public void clearSpawner(final Block block) {
        final BlockState state = block.getState();
        if (state == null || !(block instanceof CreatureSpawner)) {
            return;
        }
        try {
            final CraftCreatureSpawner spawner = (CraftCreatureSpawner)state;
            new Reflector(spawner).getSpawnerTileEntity().getSpawner().setMobName((MinecraftKey)null);
        }
        catch (Exception ex) {}
    }
    
    @Override
    public boolean isNormalCube(final World world, final Vector at) {
        final WorldServer ws = ((CraftWorld)world).getHandle();
        final IBlockData state = ws.getType(new BlockPosition(at.getBlockX(), at.getBlockY(), at.getBlockZ()));
        return state.l();
    }
    
    static {
        NMSUtilImpl.isSpigot = true;
        try {
            (NMSUtilImpl.entityCountField = Chunk.class.getDeclaredField("entityCount")).setAccessible(true);
        }
        catch (Exception ex) {}
    }
}

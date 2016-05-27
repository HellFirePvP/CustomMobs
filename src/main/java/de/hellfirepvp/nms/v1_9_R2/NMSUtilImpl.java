package de.hellfirepvp.nms.v1_9_R2;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.nms.NMSUtils;
import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.Blocks;
import net.minecraft.server.v1_9_R2.EntityInsentient;
import net.minecraft.server.v1_9_R2.EntityLiving;
import net.minecraft.server.v1_9_R2.EnumCreatureType;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.IBlockData;
import net.minecraft.server.v1_9_R2.IBlockState;
import net.minecraft.server.v1_9_R2.Item;
import net.minecraft.server.v1_9_R2.ItemStack;
import net.minecraft.server.v1_9_R2.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.spigotmc.SpigotWorldConfig;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: EntityUtilImpl
 * Created by HellFirePvP
 * Date: 26.05.2016 / 17:30
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

    @Override
    public SpigotWorldConfig getSpigotConfiguration(World world) {
        return ((CraftWorld) world).getHandle().spigotConfig;
    }

    @Override
    public int getLivingCount(WorldSpawner.CreatureType creatureType, World world) {
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
        WorldServer ws = ((CraftWorld) world).getHandle();
        return ws.a(type.a());
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

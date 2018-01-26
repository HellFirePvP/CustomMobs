package de.hellfirepvp.nms;

import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.spigotmc.SpigotWorldConfig;

import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NMSEntityUtils
 * Created by HellFirePvP
 * Date: 26.05.2016 / 17:30
 */
public interface NMSUtils {

    public void setName(LivingEntity entity, String name);

    public String getName(LivingEntity entity);

    public byte[] getMobSpawnRangeAndViewDistance(World world);

    public int getLivingCount(WorldSpawner.CreatureType creatureType, List<WorldSpawner.ChunkCoordPair> chCoords, World world);

    public boolean isPlayerInRange(Location location, double range);

    public void clearSpawner(Block block);

    public boolean isNormalCube(World world, Vector at);

}

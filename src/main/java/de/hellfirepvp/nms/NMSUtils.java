package de.hellfirepvp.nms;

import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.Location;
import java.util.List;
import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public interface NMSUtils
{
    void setName(final LivingEntity p0, final String p1);
    
    String getName(final LivingEntity p0);
    
    byte[] getMobSpawnRangeAndViewDistance(final World p0);
    
    int getLivingCount(final WorldSpawner.CreatureType p0, final List<WorldSpawner.ChunkCoordPair> p1, final World p2);
    
    boolean isPlayerInRange(final Location p0, final double p1);
    
    void clearSpawner(final Block p0);
    
    boolean isNormalCube(final World p0, final Vector p1);
}

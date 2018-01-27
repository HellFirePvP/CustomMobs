package de.hellfirepvp.spawning.worldSpawning;

import java.util.HashMap;
import java.util.Collection;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import de.hellfirepvp.util.WorldBorderUtil;
import org.bukkit.util.Vector;
import java.util.Iterator;
import org.bukkit.Material;
import de.hellfirepvp.util.LocationUtils;
import org.bukkit.Location;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import de.hellfirepvp.CustomMobs;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WorldSpawner
{
    private final Random rand;
    private List<ChunkCoordPair> chCoords;
    
    public WorldSpawner() {
        this.rand = new Random();
        this.chCoords = new LinkedList<ChunkCoordPair>();
    }
    
    public void start() {
        final int wsTickSpeed = CustomMobs.instance.getConfigHandler().worldSpawnerTickSpeed();
        Bukkit.getScheduler().runTaskTimer((Plugin)CustomMobs.instance, this::doTick, (long)wsTickSpeed, (long)wsTickSpeed);
    }
    
    private void doTick() {
        final int spawnThreshold = CustomMobs.instance.getConfigHandler().spawnThreshold();
        for (final World w : Bukkit.getWorlds()) {
            if (w.getPlayers().size() == 0) {
                continue;
            }
            if (w.getGameRuleValue("doMobSpawning").equalsIgnoreCase("false")) {
                continue;
            }
            this.chCoords.clear();
            final long time = w.getTime();
            final long ticksPerAnimalSpawns = w.getTicksPerAnimalSpawns();
            final long ticksPerMonsterSpawns = w.getTicksPerMonsterSpawns();
            final boolean spawnHostileMobs = w.getAllowMonsters() && ticksPerMonsterSpawns != 0L && time % ticksPerMonsterSpawns == 0L;
            final boolean spawnPeacefulMobs = w.getAllowAnimals() && ticksPerAnimalSpawns != 0L && time % ticksPerAnimalSpawns == 0L;
            if (!spawnPeacefulMobs && !spawnHostileMobs) {
                continue;
            }
            final boolean spawnOnSetTime = time % 400L == 0L;
            this.chCoords.clear();
            final int collectedChunksAmount = this.collectChunks(w, this.chCoords);
            final Location spawn = w.getSpawnLocation();
            for (final CreatureType type : CreatureType.values()) {
                final int limit = type.getLimit(w);
                if (limit > 0 && (!type.isPeaceful() || spawnPeacefulMobs) && (type.isPeaceful() || spawnHostileMobs) && (!type.isAnimal() || spawnOnSetTime)) {
                    final int alive = type.getLivingCount(this.chCoords, w);
                    final int cap = limit * collectedChunksAmount / spawnThreshold;
                    if (alive < cap) {
                        for (final ChunkCoordPair chCoord : this.chCoords) {
                            final int z = chCoord.z;
                            final int x = chCoord.x;
                            final Vector pos = this.getRandomChunkPosition(w, x, z);
                            if (pos != null && NMSReflector.nmsUtils.isNormalCube(w, pos)) {
                                for (int i = 0; i < 3; ++i) {
                                    int pX = pos.getBlockX();
                                    int pY = pos.getBlockY();
                                    int pZ = pos.getBlockZ();
                                    final int range = 6;
                                    for (int it = ceiling_double_int(Math.random() * 4.0), j = 0; j < it; ++j) {
                                        pX += this.rand.nextInt(range) - this.rand.nextInt(range);
                                        pY += this.rand.nextInt(1) - this.rand.nextInt(1);
                                        pZ += this.rand.nextInt(range) - this.rand.nextInt(range);
                                        Location at = new Location(w, (double)pX, (double)pY, (double)pZ);
                                        at = LocationUtils.toRand(at, 4);
                                        if (w.getBlockAt(at).getType().equals((Object)Material.AIR) && w.getBlockAt(at.clone().add(0.0, 1.0, 0.0)).getType().equals((Object)Material.AIR) && !NMSReflector.nmsUtils.isPlayerInRange(at, 24.0) && at.distanceSquared(spawn) >= 576.0 && CustomMobs.instance.getWorldSpawnExecutor().shouldSpawnCustomMobNext()) {
                                            CustomMobs.instance.getWorldSpawnExecutor().handleMobSpawning(at);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static int ceiling_double_int(final double value) {
        final int i = (int)value;
        return (value > i) ? (i + 1) : i;
    }
    
    private Vector getRandomChunkPosition(final World worldIn, final int x, final int z) {
        final int i = x * 16 + this.rand.nextInt(16);
        final int j = z * 16 + this.rand.nextInt(16);
        final int highest = worldIn.getHighestBlockYAt(i, j);
        if (highest <= 0) {
            return null;
        }
        return new Vector(i, this.rand.nextInt(highest), j);
    }
    
    private int collectChunks(final World world, final List<ChunkCoordPair> chunks) {
        final byte[] spawnDetails = NMSReflector.nmsUtils.getMobSpawnRangeAndViewDistance(world);
        final WorldBorderUtil worldBorderUtil = new WorldBorderUtil(world.getWorldBorder());
        int counter = 0;
        for (final Player p : world.getPlayers()) {
            if (p.getGameMode().equals((Object)GameMode.SPECTATOR)) {
                continue;
            }
            final Chunk ch = p.getLocation().getChunk();
            final int chX = ch.getX();
            final int chZ = ch.getZ();
            byte viewRange = spawnDetails[0];
            viewRange = ((viewRange > spawnDetails[1]) ? spawnDetails[1] : viewRange);
            viewRange = (byte)((viewRange > 8) ? 8 : viewRange);
            for (int xx = -viewRange; xx <= viewRange; ++xx) {
                for (int zz = -viewRange; zz < viewRange; ++zz) {
                    if (worldBorderUtil.isInside(chX + xx, chZ + zz)) {
                        final ChunkCoordPair pair = new ChunkCoordPair(chX + xx, chZ + zz);
                        if (!chunks.contains(pair)) {
                            ++counter;
                            chunks.add(pair);
                        }
                    }
                }
            }
        }
        return counter;
    }
    
    public static class ChunkCoordPair
    {
        public final int x;
        public final int z;
        
        public ChunkCoordPair(final int x, final int z) {
            this.x = x;
            this.z = z;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final ChunkCoordPair that = (ChunkCoordPair)o;
            return this.x == that.x && this.z == that.z;
        }
        
        @Override
        public int hashCode() {
            int result = this.x;
            result = 31 * result + this.z;
            return result;
        }
    }
    
    public enum CreatureType
    {
        AMBIENT("AMBIENT", true, false), 
        WATER_CREATURE("WATER_CREATURE", true, false), 
        ANIMAL("CREATURE", true, true), 
        MONSTER("MONSTER", false, false);
        
        private static Map<String, CreatureType> BY_NAME;
        private final boolean peaceful;
        private final boolean animal;
        private final String representation;
        
        private CreatureType(final String representation, final boolean peaceful, final boolean animal) {
            this.peaceful = peaceful;
            this.animal = animal;
            this.representation = representation;
        }
        
        public String getName() {
            return this.representation;
        }
        
        public boolean isAnimal() {
            return this.animal;
        }
        
        public boolean isPeaceful() {
            return this.peaceful;
        }
        
        public int getLivingCount(final List<ChunkCoordPair> chCoords, final World world) {
            return NMSReflector.nmsUtils.getLivingCount(this, chCoords, world);
        }
        
        public int getLimit(final World world) {
            switch (this) {
                case AMBIENT: {
                    return world.getAmbientSpawnLimit();
                }
                case WATER_CREATURE: {
                    return world.getWaterAnimalSpawnLimit();
                }
                case ANIMAL: {
                    return world.getAnimalSpawnLimit();
                }
                case MONSTER: {
                    return world.getMonsterSpawnLimit();
                }
                default: {
                    return 0;
                }
            }
        }
        
        public static CreatureType getByName(final String name) {
            return CreatureType.BY_NAME.get(name);
        }
        
        public static Collection<String> getNames() {
            return CreatureType.BY_NAME.keySet();
        }
        
        static {
            CreatureType.BY_NAME = new HashMap<String, CreatureType>();
            for (final CreatureType ct : values()) {
                if (ct != null) {
                    CreatureType.BY_NAME.put(ct.getName(), ct);
                }
            }
        }
    }
}

package de.hellfirepvp.spawning.worldSpawning;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.util.LocationUtils;
import de.hellfirepvp.util.WorldBorderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: WorldSpawner
 * Created by HellFirePvP
 * Date: 26.05.2016 / 18:12
 */
public class WorldSpawner {

    private final Random rand = new Random();
    private List<ChunkCoordPair> chCoords = new LinkedList<>();

    public void start() {
        int wsTickSpeed = CustomMobs.instance.getConfigHandler().worldSpawnerTickSpeed();
        Bukkit.getScheduler().runTaskTimer(CustomMobs.instance, this::doTick, wsTickSpeed, wsTickSpeed);
    }

    private void doTick() {
        int spawnThreshold = CustomMobs.instance.getConfigHandler().spawnThreshold();
        for (World w : Bukkit.getWorlds()) {
            if(w.getPlayers().size() == 0) continue;
            if(w.getGameRuleValue("doMobSpawning").equalsIgnoreCase("false")) continue;

            chCoords.clear();

            long time = w.getTime();
            long ticksPerAnimalSpawns = w.getTicksPerAnimalSpawns();
            long ticksPerMonsterSpawns = w.getTicksPerMonsterSpawns();
            boolean spawnHostileMobs = ((w.getAllowMonsters()) && (ticksPerMonsterSpawns != 0L) && (time % ticksPerMonsterSpawns == 0L));
            boolean spawnPeacefulMobs = ((w.getAllowAnimals()) && (ticksPerAnimalSpawns != 0L) && (time % ticksPerAnimalSpawns == 0L));
            if(!spawnPeacefulMobs && !spawnHostileMobs) continue;
            boolean spawnOnSetTime = time % 400 == 0;

            chCoords.clear();
            int collectedChunksAmount = collectChunks(w, chCoords);
            Location spawn = w.getSpawnLocation();
            for (CreatureType type : CreatureType.values()) {
                int limit = type.getLimit(w);
                if(limit > 0) {
                    if((!type.isPeaceful() || spawnPeacefulMobs) && (type.isPeaceful() || spawnHostileMobs) && (!type.isAnimal() || spawnOnSetTime)) {
                        int alive = type.getLivingCount(chCoords, w);
                        int cap = (limit * collectedChunksAmount) / spawnThreshold;
                        if(alive < cap) {
                            for (ChunkCoordPair chCoord : chCoords) {
                                int z = chCoord.z;
                                int x = chCoord.x;
                                Vector pos = getRandomChunkPosition(w, x, z);
                                if(pos != null && NMSReflector.nmsUtils.isNormalCube(w, pos)) {
                                    for (int i = 0; i < 3; ++i) {
                                        int pX = pos.getBlockX();
                                        int pY = pos.getBlockY();
                                        int pZ = pos.getBlockZ();
                                        int range = 6;

                                        int it = ceiling_double_int(Math.random() * 4D);
                                        for (int j = 0; j < it; ++j) {
                                            pX += rand.nextInt(range) - rand.nextInt(range);
                                            pY += rand.nextInt(1)     - rand.nextInt(1);
                                            pZ += rand.nextInt(range) - rand.nextInt(range);
                                            Location at = new Location(w, pX, pY, pZ);
                                            at = LocationUtils.toRand(at, 4);

                                            if (w.getBlockAt(at).getType().equals(Material.AIR) &&
                                                    w.getBlockAt(at.clone().add(0, 1, 0)).getType().equals(Material.AIR) &&
                                                    !NMSReflector.nmsUtils.isPlayerInRange(at, 24D) && at.distanceSquared(spawn) >= 576.0D) {
                                                if(CustomMobs.instance.getWorldSpawnExecutor().shouldSpawnCustomMobNext()) {
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
        }
    }

    //Taken from MathHelper
    public static int ceiling_double_int(double value) {
        int i = (int)value;
        return value > (double)i ? i + 1 : i;
    }

    private Vector getRandomChunkPosition(World worldIn, int x, int z) {
        int i = x * 16 + rand.nextInt(16);
        int j = z * 16 + rand.nextInt(16);
        int highest = worldIn.getHighestBlockYAt(i, j);
        if(highest <= 0) return null;
        return new Vector(i, rand.nextInt(highest), j);
    }

    private int collectChunks(World world, List<ChunkCoordPair> chunks) {
        byte[] spawnDetails = NMSReflector.nmsUtils.getMobSpawnRangeAndViewDistance(world);
        WorldBorderUtil worldBorderUtil = new WorldBorderUtil(world.getWorldBorder());
        int counter = 0;
        for (Player p : world.getPlayers()) {
            if(p.getGameMode().equals(GameMode.SPECTATOR)) continue;

            Chunk ch = p.getLocation().getChunk();
            int chX = ch.getX();
            int chZ = ch.getZ();

            byte viewRange = spawnDetails[0];
            viewRange = viewRange > spawnDetails[1] ? spawnDetails[1] : viewRange;
            viewRange = viewRange > 8 ? 8 : viewRange;
            for (int xx = -viewRange; xx <= viewRange; xx++) {
                for (int zz = -viewRange; zz < viewRange; zz++) {

                    if(!worldBorderUtil.isInside(chX + xx, chZ + zz)) {
                        continue;
                    }

                    ChunkCoordPair pair = new ChunkCoordPair(chX + xx, chZ + zz);
                    if(!chunks.contains(pair)) {
                        counter++;
                        chunks.add(pair);
                    }
                }
            }
        }
        return counter;
    }

    public static class ChunkCoordPair {

        public final int x, z;

        public ChunkCoordPair(int x, int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkCoordPair that = (ChunkCoordPair) o;
            return x == that.x && z == that.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + z;
            return result;
        }
    }

    public static enum CreatureType {

        AMBIENT("AMBIENT", true, false),
        WATER_CREATURE("WATER_CREATURE", true, false),
        ANIMAL("CREATURE", true, true),
        MONSTER("MONSTER", false, false);

        private static Map<String, CreatureType> BY_NAME = new HashMap<>();

        private final boolean peaceful, animal;
        private final String representation;

        private CreatureType(String representation, boolean peaceful, boolean animal) {
            this.peaceful = peaceful;
            this.animal = animal;
            this.representation = representation;
        }

        public String getName() {
            return representation;
        }

        public boolean isAnimal() {
            return animal;
        }

        public boolean isPeaceful() {
            return peaceful;
        }

        public int getLivingCount(List<ChunkCoordPair> chCoords, World world) {
            return NMSReflector.nmsUtils.getLivingCount(this, chCoords, world);
        }

        public int getLimit(World world) {
            switch (this) {
                case AMBIENT:
                    return world.getAmbientSpawnLimit();
                case WATER_CREATURE:
                    return world.getWaterAnimalSpawnLimit();
                case ANIMAL:
                    return world.getAnimalSpawnLimit();
                case MONSTER:
                    return world.getMonsterSpawnLimit();
            }
            return 0;
        }

        public static CreatureType getByName(String name) {
            return BY_NAME.get(name);
        }

        public static Collection<String> getNames() {
            return BY_NAME.keySet();
        }

        static {
            for (CreatureType ct : values()) {
                if(ct == null) continue;
                BY_NAME.put(ct.getName(), ct);
            }
        }

    }

}

package de.hellfirepvp.nms;

import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: BiomeMetaProvider
 * Created by HellFirePvP
 * Date: 24.05.2016 / 22:45
 */
public interface BiomeMetaProvider {

    public List getBiomes();

    public Map<String, Map<String, List<NMSBiomeMetaLink>>> getSortedBiomeMeta();

    public boolean applyBiomeData(Map<String, Map<String, List<NMSBiomeMetaLink>>> data);

    public static class NMSBiomeMetaLink {

        public final int weightedMobChance, minimumCount, maximumCount;
        public final String entityClassStr;

        public NMSBiomeMetaLink(int weightedMobChance, int minimumCount, int maximumCount, String entityClassStr) {
            this.weightedMobChance = weightedMobChance;
            this.minimumCount = minimumCount;
            this.maximumCount = maximumCount;
            this.entityClassStr = entityClassStr;
        }
    }

}

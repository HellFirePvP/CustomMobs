package de.hellfirepvp.nms;

import java.util.Map;
import java.util.List;

public interface BiomeMetaProvider
{
    List getBiomes();
    
    Map<String, Map<String, List<NMSBiomeMetaLink>>> getSortedBiomeMeta();
    
    boolean applyBiomeData(final Map<String, Map<String, List<NMSBiomeMetaLink>>> p0);
    
    public static class NMSBiomeMetaLink
    {
        public final int weightedMobChance;
        public final int minimumCount;
        public final int maximumCount;
        public final String entityClassStr;
        
        public NMSBiomeMetaLink(final int weightedMobChance, final int minimumCount, final int maximumCount, final String entityClassStr) {
            this.weightedMobChance = weightedMobChance;
            this.minimumCount = minimumCount;
            this.maximumCount = maximumCount;
            this.entityClassStr = entityClassStr;
        }
    }
}

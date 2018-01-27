package de.hellfirepvp.data;

import de.hellfirepvp.file.read.FullControlBiomesReader;
import de.hellfirepvp.CustomMobs;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.HashMap;
import de.hellfirepvp.nms.BiomeMetaProvider;
import java.util.List;
import java.util.Map;

public final class FullControlHandler
{
    private Map<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>> spawnBehavior;
    private Map<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>> original;
    
    public FullControlHandler() {
        this.spawnBehavior = new HashMap<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>>();
        this.original = null;
    }
    
    public void pushDefaultData(final YamlConfiguration out) {
        if (this.original == null) {
            this.original = NMSReflector.biomeMetaProvider.getSortedBiomeMeta();
        }
        final Map<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>> sortedBiomeMeta = new HashMap<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>>(this.original);
        for (final String biomeName : sortedBiomeMeta.keySet()) {
            final ConfigurationSection biomeSection = out.createSection(biomeName);
            final Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>> typeMap = sortedBiomeMeta.get(biomeName);
            for (final String creatureType : typeMap.keySet()) {
                final ConfigurationSection sectionCType = biomeSection.createSection(creatureType);
                final List<BiomeMetaProvider.NMSBiomeMetaLink> biomeMetaLinks = typeMap.get(creatureType);
                for (final BiomeMetaProvider.NMSBiomeMetaLink link : biomeMetaLinks) {
                    final ConfigurationSection metaSection = sectionCType.createSection(link.entityClassStr);
                    metaSection.set("spawnMinimum", (Object)link.minimumCount);
                    metaSection.set("spawnMaximum", (Object)link.maximumCount);
                    metaSection.set("weightedRandomChance", (Object)link.weightedMobChance);
                }
            }
        }
    }
    
    public void readAndPushData() {
        if (CustomMobs.instance.getConfigHandler().useFullControl()) {
            CustomMobs.logger.info("Loading Fullcontrol data...");
            if (this.original == null) {
                this.original = NMSReflector.biomeMetaProvider.getSortedBiomeMeta();
            }
            this.spawnBehavior.clear();
            FullControlBiomesReader.readBiomes(this.spawnBehavior);
            NMSReflector.biomeMetaProvider.applyBiomeData(this.spawnBehavior);
            CustomMobs.logger.info("Applied biome settings to minecraft!");
        }
    }
    
    public void restoreMCDefault() {
        if (CustomMobs.instance.getConfigHandler().useFullControl()) {
            if (this.original == null) {
                CustomMobs.logger.info("Fullcontrol was unable to clean up. Restarting your server fixxes the problem");
                return;
            }
            NMSReflector.biomeMetaProvider.applyBiomeData(this.original);
            CustomMobs.logger.info("Fullcontrol cleaned up.");
        }
    }
}

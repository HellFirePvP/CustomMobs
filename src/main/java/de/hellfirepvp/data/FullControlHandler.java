package de.hellfirepvp.data;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.file.read.FullControlBiomesReader;
import de.hellfirepvp.nms.BiomeMetaProvider;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * HellFirePvP@Admin
 * Date: 31.05.2015 / 23:21
 * on Project CustomMobs
 * FullControlHandler
 */
public final class FullControlHandler {

    private Map<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>> spawnBehavior = new HashMap<>();

    private Map<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>> original = null;

    public FullControlHandler() {}

    public void pushDefaultData(YamlConfiguration out) {
        //Getting sorted meta works because when normally executed, this contains the default metas...
        if(original == null) {
            original = NMSReflector.biomeMetaProvider.getSortedBiomeMeta();
        }
        Map<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>> sortedBiomeMeta = new HashMap<>(original);
        for(String biomeName : sortedBiomeMeta.keySet()) {
            ConfigurationSection biomeSection = out.createSection(biomeName);
            Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>> typeMap = sortedBiomeMeta.get(biomeName);
            for(String creatureType : typeMap.keySet()) {
                ConfigurationSection sectionCType = biomeSection.createSection(creatureType);
                List<BiomeMetaProvider.NMSBiomeMetaLink> biomeMetaLinks = typeMap.get(creatureType);
                for(BiomeMetaProvider.NMSBiomeMetaLink link : biomeMetaLinks) {
                    ConfigurationSection metaSection = sectionCType.createSection(link.entityClassStr);
                    metaSection.set(FULLCONTROL_DATA_SPAWNCOUNT_MINIMUM, link.minimumCount);
                    metaSection.set(FULLCONTROL_DATA_SPAWNCOUNT_MAXIMUM, link.maximumCount);
                    metaSection.set(FULLCONTROL_DATA_WEIGHTEDRANDOM_CHANCE, link.weightedMobChance);
                }
            }
        }
    }


    public void readAndPushData() {
        if(CustomMobs.instance.getConfigHandler().useFullControl()) {
            CustomMobs.logger.info("Loading Fullcontrol data...");

            if(original == null) {
                original = NMSReflector.biomeMetaProvider.getSortedBiomeMeta();
            }

            spawnBehavior.clear();
            FullControlBiomesReader.readBiomes(this.spawnBehavior);

            NMSReflector.biomeMetaProvider.applyBiomeData(this.spawnBehavior);
            CustomMobs.logger.info("Applied biome settings to minecraft!");
        }
    }

    public void restoreMCDefault() {
        if(CustomMobs.instance.getConfigHandler().useFullControl()) {

            if(original == null) {
                CustomMobs.logger.info("Fullcontrol was unable to clean up. Restarting your server fixxes the problem");
                return;
            }

            NMSReflector.biomeMetaProvider.applyBiomeData(original);

            CustomMobs.logger.info("Fullcontrol cleaned up.");
        }
    }
}

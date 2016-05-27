package de.hellfirepvp.file.read;

import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.nms.BiomeMetaProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hellfirepvp.lib.LibConstantKeys.*;

/**
 * HellFirePvP@Admin
 * Date: 01.06.2015 / 09:59
 * on Project CustomMobs
 * FullControlBiomesReader
 */
public class FullControlBiomesReader {

    public static void readBiomes(Map<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>> out) {
        YamlConfiguration cfg = LibConfiguration.getFullcontrolBiomeConfiguration();

        for(String biomeName : cfg.getKeys(false)) {
            if(biomeName == null || biomeName.isEmpty()) continue;
            ConfigurationSection section = cfg.getConfigurationSection(biomeName);
            if(section == null) continue;

            Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>> biomeBehavior = new HashMap<>();

            for(String enumCreatureTypeTarget : section.getKeys(false)) {
                if(enumCreatureTypeTarget == null || enumCreatureTypeTarget.isEmpty()) continue;
                ConfigurationSection creatureTypeSection = section.getConfigurationSection(enumCreatureTypeTarget);
                if(creatureTypeSection == null) continue;

                List<BiomeMetaProvider.NMSBiomeMetaLink> specifiedMetas = new ArrayList<>();

                for(String mobTypeName : creatureTypeSection.getKeys(false)) {
                    if(mobTypeName == null || mobTypeName.isEmpty()) continue;
                    ConfigurationSection mobSection = creatureTypeSection.getConfigurationSection(mobTypeName);
                    if(mobSection == null) continue;

                    int minimum = mobSection.getInt(FULLCONTROL_DATA_SPAWNCOUNT_MINIMUM, 2);
                    int maximum = mobSection.getInt(FULLCONTROL_DATA_SPAWNCOUNT_MAXIMUM, 4);
                    int chance = mobSection.getInt(FULLCONTROL_DATA_WEIGHTEDRANDOM_CHANCE, 4);

                    specifiedMetas.add(new BiomeMetaProvider.NMSBiomeMetaLink(chance, minimum, maximum, mobTypeName));
                }

                biomeBehavior.put(enumCreatureTypeTarget, specifiedMetas);
            }

            out.put(biomeName, biomeBehavior);
        }
    }

}

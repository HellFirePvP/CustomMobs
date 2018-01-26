package de.hellfirepvp.file.read;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.nms.BiomeMetaProvider;
import java.util.List;
import java.util.Map;

public class FullControlBiomesReader
{
    public static void readBiomes(final Map<String, Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>> out) {
        final YamlConfiguration cfg = LibConfiguration.getFullcontrolBiomeConfiguration();
        for (final String biomeName : cfg.getKeys(false)) {
            if (biomeName != null) {
                if (biomeName.isEmpty()) {
                    continue;
                }
                final ConfigurationSection section = cfg.getConfigurationSection(biomeName);
                if (section == null) {
                    continue;
                }
                final Map<String, List<BiomeMetaProvider.NMSBiomeMetaLink>> biomeBehavior = new HashMap<String, List<BiomeMetaProvider.NMSBiomeMetaLink>>();
                for (final String enumCreatureTypeTarget : section.getKeys(false)) {
                    if (enumCreatureTypeTarget != null) {
                        if (enumCreatureTypeTarget.isEmpty()) {
                            continue;
                        }
                        final ConfigurationSection creatureTypeSection = section.getConfigurationSection(enumCreatureTypeTarget);
                        if (creatureTypeSection == null) {
                            continue;
                        }
                        final List<BiomeMetaProvider.NMSBiomeMetaLink> specifiedMetas = new ArrayList<BiomeMetaProvider.NMSBiomeMetaLink>();
                        for (final String mobTypeName : creatureTypeSection.getKeys(false)) {
                            if (mobTypeName != null) {
                                if (mobTypeName.isEmpty()) {
                                    continue;
                                }
                                final ConfigurationSection mobSection = creatureTypeSection.getConfigurationSection(mobTypeName);
                                if (mobSection == null) {
                                    continue;
                                }
                                final int minimum = mobSection.getInt("spawnMinimum", 2);
                                final int maximum = mobSection.getInt("spawnMaximum", 4);
                                final int chance = mobSection.getInt("weightedRandomChance", 4);
                                specifiedMetas.add(new BiomeMetaProvider.NMSBiomeMetaLink(chance, minimum, maximum, mobTypeName));
                            }
                        }
                        biomeBehavior.put(enumCreatureTypeTarget, specifiedMetas);
                    }
                }
                out.put(biomeName, biomeBehavior);
            }
        }
    }
}

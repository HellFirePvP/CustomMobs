package de.hellfirepvp.nms.v1_10_R1;

import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityTypes;
import de.hellfirepvp.nms.NMSReflector;
import net.minecraft.server.v1_10_R1.WeightedRandom;
import net.minecraft.server.v1_10_R1.EnumCreatureType;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import net.minecraft.server.v1_10_R1.BiomeBase;
import java.util.List;
import de.hellfirepvp.nms.BiomeMetaProvider;

public class BiomeMetaProviderImpl implements BiomeMetaProvider
{
    @Override
    public List getBiomes() {
        return new LinkedList(BiomeBase.i);
    }
    
    @Override
    public Map<String, Map<String, List<NMSBiomeMetaLink>>> getSortedBiomeMeta() {
        final Map<String, Map<String, List<NMSBiomeMetaLink>>> dataOutput = new HashMap<String, Map<String, List<NMSBiomeMetaLink>>>();
        for (final BiomeBase base : new LinkedList<>(BiomeBase.i)) {
            if (base == null) {
                continue;
            }
            final String name = base.l();
            final Map<String, List<NMSBiomeMetaLink>> specData = new HashMap<String, List<NMSBiomeMetaLink>>();
            for (final EnumCreatureType type : EnumCreatureType.values()) {
                if (type != null) {
                    final List biomeMetaList = base.getMobs(type);
                    specData.put(type.name(), this.getNMSRepresentation(biomeMetaList));
                }
            }
            dataOutput.put(name, specData);
        }
        return dataOutput;
    }
    
    private List<NMSBiomeMetaLink> getNMSRepresentation(final List metas) {
        final List<NMSBiomeMetaLink> biomeMetaLinks = new LinkedList<NMSBiomeMetaLink>();
        for (final Object objMeta : metas) {
            if (objMeta != null) {
                if (!(objMeta instanceof BiomeBase.BiomeMeta)) {
                    continue;
                }
                final BiomeBase.BiomeMeta meta = (BiomeBase.BiomeMeta)objMeta;
                final int weightedChance = NMSReflector.getField("a", WeightedRandom.WeightedRandomChoice.class, meta, Integer.TYPE);
                final String typeStr = this.getEntityTypeString(meta.b);
                if (typeStr == null) {
                    continue;
                }
                biomeMetaLinks.add(new NMSBiomeMetaLink(weightedChance, meta.c, meta.d, typeStr));
            }
        }
        return biomeMetaLinks;
    }
    
    private String getEntityTypeString(final Class<?> entityClass) {
        final Map classToString = NMSReflector.getField("d", EntityTypes.class, null, Map.class);
        return String.valueOf(classToString.get(entityClass));
    }
    
    private Class<? extends EntityInsentient> getEntityTypeClass(final String name) {
        final Map stringToClass = NMSReflector.getField("c", EntityTypes.class, null, Map.class);
        try {
            return (Class<? extends EntityInsentient>) stringToClass.get(name);
        }
        catch (Throwable tr) {
            return null;
        }
    }
    
    @Override
    public boolean applyBiomeData(final Map<String, Map<String, List<NMSBiomeMetaLink>>> data) {
        try {
            for (final String biomeName : data.keySet()) {
                if (biomeName == null) {
                    continue;
                }
                final BiomeBase base = this.getBiomeBaseByName(biomeName);
                if (base == null) {
                    continue;
                }
                final Map<String, List<NMSBiomeMetaLink>> specData = data.get(biomeName);
                for (final String target : specData.keySet()) {
                    if (target == null) {
                        continue;
                    }
                    EnumCreatureType type;
                    try {
                        type = EnumCreatureType.valueOf(target);
                    }
                    catch (Throwable tr) {
                        continue;
                    }
                    if (type == null) {
                        continue;
                    }
                    final List<BiomeBase.BiomeMeta> metas = this.getMetaList(specData.get(target));
                    final List originalBiomeMetas = base.getMobs(type);
                    originalBiomeMetas.clear();
                    originalBiomeMetas.addAll(metas);
                }
            }
            return true;
        }
        catch (Throwable tr2) {
            return false;
        }
    }
    
    private List<BiomeBase.BiomeMeta> getMetaList(final List<NMSBiomeMetaLink> links) {
        final List<BiomeBase.BiomeMeta> metas = new LinkedList<BiomeBase.BiomeMeta>();
        for (final NMSBiomeMetaLink link : links) {
            if (link == null) {
                continue;
            }
            final Class<? extends EntityInsentient> entityClass = this.getEntityTypeClass(link.entityClassStr);
            if (entityClass == null) {
                continue;
            }
            metas.add(new BiomeBase.BiomeMeta((Class)entityClass, link.weightedMobChance, link.minimumCount, link.maximumCount));
        }
        return metas;
    }
    
    private BiomeBase getBiomeBaseByName(final String name) {
        for (final BiomeBase base : new LinkedList<BiomeBase>(BiomeBase.i)) {
            if (base == null) {
                continue;
            }
            if (base.l().equalsIgnoreCase(name)) {
                return base;
            }
        }
        return null;
    }
}

package de.hellfirepvp.nms;

import org.bukkit.Bukkit;
import java.lang.reflect.Field;
import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.nbt.base.NBTProvider;

public class NMSReflector
{
    public static NBTProvider nbtProvider;
    public static MobTypeProvider mobTypeProvider;
    public static BiomeMetaProvider biomeMetaProvider;
    public static NMSUtils nmsUtils;
    public static final String VERSION;
    
    public static boolean initialize() {
        try {
            NMSReflector.nbtProvider = (NBTProvider)Class.forName(getCmobPackageName() + ".NBTProviderImpl").newInstance();
            NMSReflector.biomeMetaProvider = (BiomeMetaProvider)Class.forName(getCmobPackageName() + ".BiomeMetaProviderImpl").newInstance();
            NMSReflector.nmsUtils = (NMSUtils)Class.forName(getCmobPackageName() + ".NMSUtilImpl").newInstance();
            if (CustomMobs.currentVersion.isThisAMoreRecentOrEqualVersionThan(SupportedVersions.V1_11_R1)) {
                NMSReflector.mobTypeProvider = (RegistryTypeProvider)Class.forName(getCmobPackageName() + ".TypeProviderImpl").newInstance();
            }
            else {
                NMSReflector.mobTypeProvider = (MobTypeProvider)Class.forName(getCmobPackageName() + ".TypeProviderImpl").newInstance();
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public static <T> T getField(final String fieldName, final Class fieldClass, final Object instance, final Class<T> toCast) {
        try {
            final Field f = fieldClass.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T)f.get(instance);
        }
        catch (Throwable tr) {
            return null;
        }
    }
    
    public static void setFinalField(final String fieldName, final Class fieldClass, final Object instance, final Object newValue) throws Exception {
        final Field f = fieldClass.getDeclaredField(fieldName);
        setFinalField(f, instance, newValue);
    }
    
    public static void setFinalField(final Field field, final Object toSet, final Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new Exception();
        }
        modifiersField.setAccessible(true);
        try {
            modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
            throw new Exception();
        }
        try {
            field.set(toSet, newValue);
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
            throw new Exception();
        }
    }
    
    public static String getCmobPackageName() {
        return "de.hellfirepvp.nms." + getVersion();
    }
    
    public static String getNMSPackageName() {
        return "net.minecraft.server." + getVersion();
    }
    
    public static String getCBPackageName() {
        return "org.bukkit.craftbukkit." + getVersion();
    }
    
    private static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }
    
    static {
        VERSION = getVersion();
    }
}

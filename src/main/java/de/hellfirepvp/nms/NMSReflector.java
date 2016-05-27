package de.hellfirepvp.nms;

import de.hellfirepvp.data.nbt.NBTProvider;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: NMSReflector
 * Created by HellFirePvP
 * Date: 23.05.2016 / 22:42
 */
public class NMSReflector {

    public static NBTProvider nbtProvider;
    public static MobTypeProvider mobTypeProvider;
    public static BiomeMetaProvider biomeMetaProvider;
    public static NMSUtils nmsUtils;
    public static final String VERSION = getVersion();

    public static boolean initialize() {
        try {
            nbtProvider = (NBTProvider) Class.forName(getCmobPackageName() + ".NBTProviderImpl").newInstance();
            mobTypeProvider = (MobTypeProvider) Class.forName(getCmobPackageName() + ".TypeProviderImpl").newInstance();
            biomeMetaProvider = (BiomeMetaProvider) Class.forName(getCmobPackageName() + ".BiomeMetaProviderImpl").newInstance();
            nmsUtils = (NMSUtils) Class.forName(getCmobPackageName() + ".NMSUtilImpl").newInstance();
        } catch (Exception e) {
            return false;
        } //Welp. no providers here.
        return true;
    }

    public static <T> T getField(String fieldName, Class fieldClass, Object instance, Class<T> toCast) {
        try {
            Field f = fieldClass.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(instance);
        } catch (Throwable tr) {
            return null;
        }
    }

    public static void setFinalField(String fieldName, Class fieldClass, Object instance, Object newValue) throws Exception {
        Field f = fieldClass.getDeclaredField(fieldName);
        setFinalField(f, instance, newValue);
    }

    public static void setFinalField(Field field, Object toSet, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new Exception();
        }
        modifiersField.setAccessible(true);
        try {
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new Exception();
        }

        try {
            field.set(toSet, newValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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

}

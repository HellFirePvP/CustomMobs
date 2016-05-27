package de.hellfirepvp.util;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: EntityUtils
 * Created by HellFirePvP
 * Date: 26.05.2016 / 15:32
 */
public class EntityUtils {

    private static Class classCraftEntity;
    private static Method methodGetHandle;
    private static Field fireProofField;

    public static void setFireproof(Entity e) {
        if(methodGetHandle == null ||
                classCraftEntity == null ||
                fireProofField == null) {
            setupReflectionContext();
        }

        try {
            Object craftEntity = classCraftEntity.cast(e);
            Object nmsEntity = methodGetHandle.invoke(craftEntity);
            fireProofField.setAccessible(true);
            fireProofField.set(nmsEntity, true);
        } catch (Exception exc) {
            CustomMobs.logger.info("Failed to set a mob fireproof.");
        }
    }

    private static void setupReflectionContext() {
        try {
            classCraftEntity = Class.forName(NMSReflector.getCBPackageName() + ".entity.CraftEntity");
            methodGetHandle = classCraftEntity.getDeclaredMethod("getHandle");
            fireProofField = Class.forName(NMSReflector.getNMSPackageName() + ".Entity").getDeclaredField("fireProof");
        } catch (Exception exc) {}
    }

}

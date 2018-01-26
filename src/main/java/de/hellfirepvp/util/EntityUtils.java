package de.hellfirepvp.util;

import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EntityUtils
{
    private static Class classCraftEntity;
    private static Method methodGetHandle;
    private static Field fireProofField;
    
    public static void setFireproof(final Entity e) {
        if (EntityUtils.methodGetHandle == null || EntityUtils.classCraftEntity == null || EntityUtils.fireProofField == null) {
            setupReflectionContext();
        }
        try {
            final Object craftEntity = EntityUtils.classCraftEntity.cast(e);
            final Object nmsEntity = EntityUtils.methodGetHandle.invoke(craftEntity, new Object[0]);
            EntityUtils.fireProofField.setAccessible(true);
            EntityUtils.fireProofField.set(nmsEntity, true);
        }
        catch (Exception exc) {
            CustomMobs.logger.info("Failed to set a mob fireproof.");
        }
    }
    
    private static void setupReflectionContext() {
        try {
            EntityUtils.classCraftEntity = Class.forName(NMSReflector.getCBPackageName() + ".entity.CraftEntity");
            EntityUtils.methodGetHandle = EntityUtils.classCraftEntity.getDeclaredMethod("getHandle", (Class[])new Class[0]);
            EntityUtils.fireProofField = Class.forName(NMSReflector.getNMSPackageName() + ".Entity").getDeclaredField("fireProof");
        }
        catch (Exception ex) {}
    }
}

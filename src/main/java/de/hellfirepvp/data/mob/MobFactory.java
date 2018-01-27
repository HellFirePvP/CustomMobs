package de.hellfirepvp.data.mob;

import java.io.File;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.file.write.MobDataWriter;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.entity.LivingEntity;

public class MobFactory
{
    public static boolean tryCreateCustomMobFromEntity(final LivingEntity le, final String name) {
        final WrappedNBTTagCompound mobTag = NMSReflector.mobTypeProvider.getDataFromEntity(le);
        if (mobTag == null) {
            return false;
        }
        MobDataWriter.writeMobFile(mobTag, MobDataWriter.getMobFile(name));
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        return true;
    }
    
    public static boolean tryDeleteMobFile(final String name) {
        final File file = getMobFile(name);
        if (!file.delete()) {
            return false;
        }
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        return true;
    }
    
    private static File getMobFile(final String name) {
        return new File(CustomMobs.instance.getMobDataFolder(), name + ".dat");
    }
}

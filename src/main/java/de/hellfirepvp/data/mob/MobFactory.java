package de.hellfirepvp.data.mob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import de.hellfirepvp.file.write.MobDataWriter;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.entity.LivingEntity;

import java.io.File;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: MobFactory
 * Created by HellFirePvP
 * Date: 26.05.2016 / 18:00
 */
public class MobFactory {

    public static boolean tryCreateCustomMobFromEntity(LivingEntity le, String name) {
        WrappedNBTTagCompound mobTag = NMSReflector.mobTypeProvider.getDataFromEntity(le);
        if(mobTag == null) return false;
        MobDataWriter.writeMobFile(mobTag, name);
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        return true;
    }

    public static boolean tryDeleteMobFile(String name) {
        File file = getMobFile(name);

        if(!file.delete())
            return false;

        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        return true;
    }

    private static File getMobFile(String name) {
        return new File(CustomMobs.instance.getMobDataFolder(), name + ".dat");
    }

}

package de.hellfirepvp.nms;

import org.bukkit.Location;
import de.hellfirepvp.api.data.nbt.WrappedNBTTagCompound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import java.util.List;

public interface MobTypeProvider
{
    void discoverMobTypes();
    
    List<String> getTypeNames();
    
    LivingEntity getEntityForName(final World p0, final String p1);
    
    LivingEntity createEntityShell(final World p0, final WrappedNBTTagCompound p1);
    
    LivingEntity spawnEntityFromSerializedData(final Location p0, final WrappedNBTTagCompound p1);
    
    WrappedNBTTagCompound getDataFromEntity(final LivingEntity p0);
    
    void mergeEntityDataInto(final LivingEntity p0, final WrappedNBTTagCompound p1);
}

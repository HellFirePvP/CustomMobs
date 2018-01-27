package de.hellfirepvp.api.internal;

import org.bukkit.entity.LivingEntity;
import de.hellfirepvp.api.data.callback.MobCreationCallback;
import java.util.Collection;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.ISpawnerEditor;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.api.data.ICustomMob;

public interface ApiHandler
{
    ICustomMob getCustomMob(final String p0);
    
    ISpawnSettingsEditor getSpawnSettingsEditor();
    
    ItemStack getCustomMobsTool();
    
    ISpawnerEditor getSpawnerEditor();
    
    IRespawnEditor getRespawnEditor();
    
    Collection<String> getKnownMobTypes();
    
    MobCreationCallback createCustomMob(final String p0, final String p1);
    
    MobCreationCallback createCustomMob(final LivingEntity p0, final String p1);
}

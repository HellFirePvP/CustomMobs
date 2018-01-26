package de.hellfirepvp;

import org.bukkit.entity.LivingEntity;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.data.mob.EntityAdapter;
import de.hellfirepvp.api.data.callback.MobCreationCallback;
import de.hellfirepvp.nms.NMSReflector;
import java.util.Collection;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.ISpawnerEditor;
import de.hellfirepvp.tool.CustomMobsTool;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.data.RespawnEditor;
import de.hellfirepvp.data.SpawnerEditor;
import de.hellfirepvp.data.SpawnSettingsEditor;
import de.hellfirepvp.api.internal.ApiHandler;

public class DefaultApiHandler implements ApiHandler
{
    private static SpawnSettingsEditor spawnSettingsEditor;
    private static SpawnerEditor spawnerEditor;
    private static RespawnEditor respawnEditor;
    
    @Override
    public ICustomMob getCustomMob(final String name) {
        final CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (mob != null) {
            return mob.createApiAdapter();
        }
        return null;
    }
    
    @Override
    public ISpawnSettingsEditor getSpawnSettingsEditor() {
        return DefaultApiHandler.spawnSettingsEditor;
    }
    
    @Override
    public ItemStack getCustomMobsTool() {
        return CustomMobsTool.getTool();
    }
    
    @Override
    public ISpawnerEditor getSpawnerEditor() {
        return DefaultApiHandler.spawnerEditor;
    }
    
    @Override
    public IRespawnEditor getRespawnEditor() {
        return DefaultApiHandler.respawnEditor;
    }
    
    @Override
    public Collection<String> getKnownMobTypes() {
        return NMSReflector.mobTypeProvider.getTypeNames();
    }
    
    @Override
    public MobCreationCallback createCustomMob(final String name, final String mobType) {
        if (!NMSReflector.mobTypeProvider.getTypeNames().contains(mobType)) {
            return MobCreationCallback.UNKNOWN_TYPE;
        }
        if (CustomMobs.instance.getMobDataHolder().getCustomMob(name) != null) {
            return MobCreationCallback.NAME_TAKEN;
        }
        final LivingEntity created = NMSReflector.mobTypeProvider.getEntityForName(EntityAdapter.getDefaultWorld(), mobType);
        if (MobFactory.tryCreateCustomMobFromEntity(created, name)) {
            return MobCreationCallback.SUCCESS;
        }
        return MobCreationCallback.FAILED;
    }
    
    @Override
    public MobCreationCallback createCustomMob(final LivingEntity livingEntity, final String name) {
        if (CustomMobs.instance.getMobDataHolder().getCustomMob(name) != null) {
            return MobCreationCallback.NAME_TAKEN;
        }
        if (MobFactory.tryCreateCustomMobFromEntity(livingEntity, name)) {
            return MobCreationCallback.SUCCESS;
        }
        return MobCreationCallback.FAILED;
    }
    
    static {
        DefaultApiHandler.spawnSettingsEditor = new SpawnSettingsEditor();
        DefaultApiHandler.spawnerEditor = new SpawnerEditor();
        DefaultApiHandler.respawnEditor = new RespawnEditor();
    }
}

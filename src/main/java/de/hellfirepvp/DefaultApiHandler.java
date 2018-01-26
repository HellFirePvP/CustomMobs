package de.hellfirepvp;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.api.data.ISpawnerEditor;
import de.hellfirepvp.api.data.callback.MobCreationCallback;
import de.hellfirepvp.api.internal.ApiHandler;
import de.hellfirepvp.data.RespawnEditor;
import de.hellfirepvp.data.SpawnSettingsEditor;
import de.hellfirepvp.data.SpawnerEditor;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.CustomMobAdapter;
import de.hellfirepvp.data.mob.EntityAdapter;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.tool.CustomMobsTool;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: DefaultApiHandler
 * Created by HellFirePvP
 * Date: 30.05.2016 / 09:11
 */
public class DefaultApiHandler implements ApiHandler {

    private static SpawnSettingsEditor spawnSettingsEditor = new SpawnSettingsEditor();
    private static SpawnerEditor spawnerEditor = new SpawnerEditor();
    private static RespawnEditor respawnEditor = new RespawnEditor();

    @Override
    public ICustomMob getCustomMob(String name) {
        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(mob != null) {
            return mob.createApiAdapter();
        }
        return null;
    }

    @Override
    public ISpawnSettingsEditor getSpawnSettingsEditor() {
        return spawnSettingsEditor;
    }

    @Override
    public ItemStack getCustomMobsTool() {
        return CustomMobsTool.getTool();
    }

    @Override
    public ISpawnerEditor getSpawnerEditor() {
        return spawnerEditor;
    }

    @Override
    public IRespawnEditor getRespawnEditor() {
        return respawnEditor;
    }

    @Override
    public Collection<String> getKnownMobTypes() {
        return NMSReflector.mobTypeProvider.getTypeNames();
    }

    @Override
    public MobCreationCallback createCustomMob(String name, String mobType) {
        if(!NMSReflector.mobTypeProvider.getTypeNames().contains(mobType)) {
            return MobCreationCallback.UNKNOWN_TYPE;
        }
        if(CustomMobs.instance.getMobDataHolder().getCustomMob(name) != null) {
            return MobCreationCallback.NAME_TAKEN;
        }
        LivingEntity created = NMSReflector.mobTypeProvider.getEntityForName(EntityAdapter.getDefaultWorld(), mobType);
        if(MobFactory.tryCreateCustomMobFromEntity(created, name)) {
            return MobCreationCallback.SUCCESS;
        } else {
            return MobCreationCallback.FAILED;
        }
    }

    @Override
    public MobCreationCallback createCustomMob(LivingEntity livingEntity, String name) {
        if(CustomMobs.instance.getMobDataHolder().getCustomMob(name) != null) {
            return MobCreationCallback.NAME_TAKEN;
        }
        if(MobFactory.tryCreateCustomMobFromEntity(livingEntity, name)) {
            return MobCreationCallback.SUCCESS;
        } else {
            return MobCreationCallback.FAILED;
        }
    }

}

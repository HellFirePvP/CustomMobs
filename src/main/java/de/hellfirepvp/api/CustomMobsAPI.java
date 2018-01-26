package de.hellfirepvp.api;

import de.hellfirepvp.api.internal.DummyApiHandler;
import org.bukkit.inventory.ItemStack;
import java.util.Collection;
import de.hellfirepvp.api.data.callback.MobCreationCallback;
import org.bukkit.entity.LivingEntity;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.ISpawnerEditor;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.internal.ApiHandler;

public class CustomMobsAPI
{
    private static ApiHandler handler;
    
    public static void setApiHandler(final ApiHandler apiHandler) {
        CustomMobsAPI.handler = apiHandler;
    }
    
    public static ICustomMob getCustomMob(final String name) {
        return CustomMobsAPI.handler.getCustomMob(name);
    }
    
    public static ISpawnSettingsEditor getSpawnSettingsEditor() {
        return CustomMobsAPI.handler.getSpawnSettingsEditor();
    }
    
    public static ISpawnerEditor getSpawnerEditor() {
        return CustomMobsAPI.handler.getSpawnerEditor();
    }
    
    public static IRespawnEditor getRespawnEditor() {
        return CustomMobsAPI.handler.getRespawnEditor();
    }
    
    public static MobCreationCallback createCustomMob(final LivingEntity livingEntity, final String name) {
        return CustomMobsAPI.handler.createCustomMob(livingEntity, name);
    }
    
    public static MobCreationCallback createCustomMob(final String name, final String mobType) {
        return CustomMobsAPI.handler.createCustomMob(name, mobType);
    }
    
    public static Collection<String> getKnownMobTypes() {
        return CustomMobsAPI.handler.getKnownMobTypes();
    }
    
    public static ItemStack getCustomMobsTool() {
        return CustomMobsAPI.handler.getCustomMobsTool();
    }
    
    static {
        CustomMobsAPI.handler = new DummyApiHandler();
    }
}

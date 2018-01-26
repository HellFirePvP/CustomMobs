package de.hellfirepvp.api.internal;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.api.data.ISpawnerEditor;
import de.hellfirepvp.api.data.callback.MobCreationCallback;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: DummyApiHandler
 * Created by HellFirePvP
 * Date: 26.05.2016 / 16:07
 */
public class DummyApiHandler implements ApiHandler {

    @Override
    public ICustomMob getCustomMob(String name) {
        return null;
    }

    @Override
    public ISpawnSettingsEditor getSpawnSettingsEditor() {
        return null;
    }

    @Override
    public ItemStack getCustomMobsTool() {
        return null;
    }

    @Override
    public ISpawnerEditor getSpawnerEditor() {
        return null;
    }

    @Override
    public IRespawnEditor getRespawnEditor() {
        return null;
    }

    @Override
    public Collection<String> getKnownMobTypes() {
        return Collections.emptyList();
    }

    @Override
    public MobCreationCallback createCustomMob(String name, String mobType) {
        return MobCreationCallback.FAILED;
    }

    @Override
    public MobCreationCallback createCustomMob(LivingEntity livingEntity, String name) {
        return MobCreationCallback.FAILED;
    }

}

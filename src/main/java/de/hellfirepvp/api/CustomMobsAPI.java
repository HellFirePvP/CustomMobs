package de.hellfirepvp.api;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.api.data.ISpawnerEditor;
import de.hellfirepvp.api.data.callback.MobCreationCallback;
import de.hellfirepvp.api.internal.ApiHandler;
import de.hellfirepvp.api.internal.DummyApiHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CustomMobsAPI
 * Created by HellFirePvP
 * Date: 24.05.2016 / 22:34
 */
public class CustomMobsAPI {

    //The api handler.
    private static ApiHandler handler = new DummyApiHandler();

    //Method to set the api handler. You don't need to call this method at any given point.
    public static void setApiHandler(ApiHandler apiHandler) {
        handler = apiHandler;
    }

    /**
     * Get a CustomMob object by a given CustomMob name.
     *
     * @param name The name of the CustomMob.
     * @return the customMob object or null, in case the mob doesn't exist.
     */
    public static ICustomMob getCustomMob(String name) {
        return handler.getCustomMob(name);
    }

    /**
     * Get the spawnSettings editor.
     * This is the api equivalent to /cspawn commands.
     *
     * @return the spawnSettings editor.
     */
    public static ISpawnSettingsEditor getSpawnSettingsEditor() {
        return handler.getSpawnSettingsEditor();
    }

    /**
     * Get the spawner editor.
     * This is the api equivalent to the /cmob spawner command.
     *
     * @return the spawner editor.
     */
    public static ISpawnerEditor getSpawnerEditor() {
        return handler.getSpawnerEditor();
    }

    /**
     * Get the respawn edtitor
     * This is the api equivalent to the /crespawn commands.
     *
     * @return the respawn editor
     */
    public static IRespawnEditor getRespawnEditor() {
        return handler.getRespawnEditor();
    }

    /**
     * Tries to create a new custommob from a already existing livingEntity.
     * Returns a Callback, containing information about the result.
     *
     * @param livingEntity the entity to create the custommob from.
     * @param name the name of the new created custommob.
     * @return a callback, containing information about the result.
     */
    public static MobCreationCallback createCustomMob(LivingEntity livingEntity, String name) {
        return handler.createCustomMob(livingEntity, name);
    }

    /**
     * Tries to create a new custommob of the given mobType and name.
     * Returns a Callback, containing information about the result.
     *
     * @param mobType the type of the custommob.
     * @param name the name of the new created custommob.
     * @return a callback, containing information about the result.
     */
    public static MobCreationCallback createCustomMob(String name, String mobType) {
        return handler.createCustomMob(name, mobType);
    }

    /**
     * @return a collection of all known mobtypes as strings.
     */
    public static Collection<String> getKnownMobTypes() {
        return handler.getKnownMobTypes();
    }

    /**
     * @return a new instance of the custommobs tool.
     */
    public static ItemStack getCustomMobsTool() {
        return handler.getCustomMobsTool();
    }

}

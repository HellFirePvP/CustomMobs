package de.hellfirepvp.api;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.api.data.ISpawnerEditor;
import de.hellfirepvp.api.internal.ApiHandler;
import de.hellfirepvp.api.internal.DummyApiHandler;
import org.bukkit.inventory.ItemStack;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CustomMobsAPI
 * Created by HellFirePvP
 * Date: 24.05.2016 / 22:34
 */
public class CustomMobsAPI {

    private static ApiHandler handler = new DummyApiHandler();

    public static void setApiHandler(ApiHandler apiHandler) {
        handler = apiHandler;
    }

    public static ICustomMob getCustomMob(String name) {
        return handler.getCustomMob(name);
    }

    public static ISpawnSettingsEditor getSpawnSettingsEditor() {
        return handler.getSpawnSettingsEditor();
    }

    public static ISpawnerEditor getSpawnerEditor() {
        return handler.getSpawnerEditor();
    }

    public static IRespawnEditor getRespawnEditor() {
        return handler.getRespawnEditor();
    }

    public static ItemStack getCustomMobsTool() {
        return handler.getCustomMobsTool();
    }

}

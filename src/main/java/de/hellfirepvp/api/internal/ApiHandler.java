package de.hellfirepvp.api.internal;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.data.IRespawnEditor;
import de.hellfirepvp.api.data.ISpawnSettingsEditor;
import de.hellfirepvp.api.data.ISpawnerEditor;
import org.bukkit.inventory.ItemStack;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ApiHandler
 * Created by HellFirePvP
 * Date: 26.05.2016 / 16:07
 */
public interface ApiHandler {

    public ICustomMob getCustomMob(String name);

    public ISpawnSettingsEditor getSpawnSettingsEditor();

    public ItemStack getCustomMobsTool();

    public ISpawnerEditor getSpawnerEditor();

    public IRespawnEditor getRespawnEditor();
}

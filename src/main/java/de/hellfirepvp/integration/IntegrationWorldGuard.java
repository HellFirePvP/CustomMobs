package de.hellfirepvp.integration;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: IntegrationWorldGuard
 * Created by HellFirePvP
 * Date: 23.05.2016 / 22:40
 */
public interface IntegrationWorldGuard {

    public List<String> getRegionNames(Location location);

    public boolean doRegionsAllowSpawning(EntityType type, Location at);

}

package de.hellfirepvp.integration.impl;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.config.ConfigHandler;
import de.hellfirepvp.integration.IntegrationWorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ImplIntegrationWorldGuard
 * Created by HellFirePvP
 * Date: 23.05.2016 / 23:25
 */
public class ImplIntegrationWorldGuard implements IntegrationWorldGuard {

    private WorldGuardPlugin plugin;
    private ConfigHandler cfg;

    public ImplIntegrationWorldGuard() {
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            plugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        } else {
            CustomMobs.logger.severe("Could not find worldguard plugin instance!");
        }
        cfg = CustomMobs.instance.getConfigHandler();
    }

    @Override
    public List<String> getRegionNames(Location location) {
        if(plugin == null) return null;
        ApplicableRegionSet set = plugin.getRegionManager(location.getWorld()).getApplicableRegions(location);
        List<String> str = new LinkedList<>();
        for(ProtectedRegion region : set) {
            str.add(region.getId());
        }
        return str;
    }

    @Override
    public boolean doRegionsAllowSpawning(EntityType type, Location at) {
        ApplicableRegionSet set = plugin.getRegionManager(at.getWorld()).getApplicableRegions(at);
        for (ProtectedRegion rg : set) {
            StateFlag.State s = rg.getFlag(DefaultFlag.MOB_SPAWNING);
            if(cfg.respectWGDenySpawnFlag() && s == StateFlag.State.DENY) {
                return false;
            }
            if(cfg.respectWGMobSpawningFlag()) {
                Set<EntityType> types = rg.getFlag(DefaultFlag.DENY_SPAWN);
                if(types != null) {
                    if(types.contains(type)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

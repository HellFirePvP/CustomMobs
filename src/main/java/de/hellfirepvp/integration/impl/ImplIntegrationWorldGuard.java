package de.hellfirepvp.integration.impl;

import java.util.Set;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.entity.EntityType;
import java.util.Iterator;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Location;
import de.hellfirepvp.CustomMobs;
import org.bukkit.Bukkit;
import de.hellfirepvp.config.ConfigHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.hellfirepvp.integration.IntegrationWorldGuard;

public class ImplIntegrationWorldGuard implements IntegrationWorldGuard
{
    private WorldGuardPlugin plugin;
    private ConfigHandler cfg;
    
    public ImplIntegrationWorldGuard() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            this.plugin = (WorldGuardPlugin)Bukkit.getPluginManager().getPlugin("WorldGuard");
        }
        else {
            CustomMobs.logger.severe("Could not find worldguard plugin instance!");
        }
        this.cfg = CustomMobs.instance.getConfigHandler();
    }
    
    @Override
    public List<String> getRegionNames(final Location location) {
        if (this.plugin == null) {
            return null;
        }
        final ApplicableRegionSet set = this.plugin.getRegionManager(location.getWorld()).getApplicableRegions(location);
        final List<String> str = new LinkedList<String>();
        for (final ProtectedRegion region : set) {
            str.add(region.getId());
        }
        return str;
    }
    
    @Override
    public boolean doRegionsAllowSpawning(final EntityType type, final Location at) {
        final ApplicableRegionSet set = this.plugin.getRegionManager(at.getWorld()).getApplicableRegions(at);
        for (final ProtectedRegion rg : set) {
            final StateFlag.State s = (StateFlag.State)rg.getFlag((Flag)DefaultFlag.MOB_SPAWNING);
            if (this.cfg.respectWGDenySpawnFlag() && s == StateFlag.State.DENY) {
                return false;
            }
            if (!this.cfg.respectWGMobSpawningFlag()) {
                continue;
            }
            final Set<EntityType> types = (Set<EntityType>)rg.getFlag((Flag)DefaultFlag.DENY_SPAWN);
            if (types != null && types.contains(type)) {
                return false;
            }
        }
        return true;
    }
}

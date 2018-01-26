package de.hellfirepvp.integration;

import org.bukkit.entity.EntityType;
import java.util.List;
import org.bukkit.Location;

public interface IntegrationWorldGuard
{
    List<String> getRegionNames(final Location p0);
    
    boolean doRegionsAllowSpawning(final EntityType p0, final Location p1);
}

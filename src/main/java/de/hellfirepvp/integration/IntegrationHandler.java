package de.hellfirepvp.integration;

import org.bukkit.Bukkit;

public class IntegrationHandler
{
    public static IntegrationWorldGuard integrationWorldGuard;
    public static IntegrationFactions integrationFactions;
    
    public static void loadIntegrations() {
        IntegrationHandler.integrationFactions = loadIntegration("Factions", "de.hellfirepvp.integration.impl.ImplIntegrationFactions");
        IntegrationHandler.integrationWorldGuard = loadIntegration("WorldGuard", "de.hellfirepvp.integration.impl.ImplIntegrationWorldGuard");
    }
    
    private static <T> T loadIntegration(final String plName, final String integrationClass) {
        if (Bukkit.getPluginManager().getPlugin(plName) == null) {
            return null;
        }
        try {
            return (T)Class.forName(integrationClass).newInstance();
        }
        catch (Throwable tr) {
            return null;
        }
    }
}

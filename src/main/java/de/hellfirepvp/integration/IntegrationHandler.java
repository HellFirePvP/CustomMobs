package de.hellfirepvp.integration;

import org.bukkit.Bukkit;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: IntegrationHandler
 * Created by HellFirePvP
 * Date: 23.05.2016 / 23:12
 */
public class IntegrationHandler {

    public static IntegrationWorldGuard integrationWorldGuard;
    public static IntegrationFactions integrationFactions;
    //public static IntegrationLibsDisguises integrationLibsDisguises;

    public static void loadIntegrations() {
        integrationFactions   = loadIntegration("Factions",   "de.hellfirepvp.integration.impl.ImplIntegrationFactions");
        integrationWorldGuard = loadIntegration("WorldGuard", "de.hellfirepvp.integration.impl.ImplIntegrationWorldGuard");
        //integrationLibsDisguises = loadIntegration("LibsDisguises", )
    }

    private static <T> T loadIntegration(String plName, String integrationClass) {
        if(Bukkit.getPluginManager().getPlugin(plName) == null) {
            return null;
        }

        try {
            return (T) Class.forName(integrationClass).newInstance();
        } catch (Throwable tr) {
            return null; //Unable to load due to missing dependencies?
        }
    }

}

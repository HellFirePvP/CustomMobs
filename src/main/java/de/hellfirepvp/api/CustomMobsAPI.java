package de.hellfirepvp.api;

import de.hellfirepvp.api.internal.ApiHandler;
import de.hellfirepvp.api.internal.DummyApiHandler;

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

}

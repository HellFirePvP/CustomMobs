package de.hellfirepvp.lib;

import org.bukkit.Material;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: LibMisc
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:03
 */
public class LibMisc {

    public static final String PERM_ALL = "custommobs.*";
    public static final String PERM_USE = "custommobs.cmduse";

    public static final String FILENAME_SPAWNERDATA = "spawnerData.yml";
    public static final String FILENAME_CONFIG = "config.yml";
    public static final String FILENAME_SPAWNSETTINGS = "spawnSettings.yml";
    public static final String FILENAME_RESPAWNSETTINGS = "respawnSettings.yml";
    public static final String FILENAME_AI_LEASH = "leashData.yml";
    public static final String FILENAME_FULLCONTROL_BIOMES = "fullcontrolBiomes.yml";

    public static final String DIRECTORY_AI = "ai";

    public static final Set<Material> TARGET_TRANSPARENT = Collections.unmodifiableSet(new HashSet<Material>() {
        {
            add(Material.AIR);
            add(Material.WATER);
            add(Material.STATIONARY_WATER);
            add(Material.LAVA);
            add(Material.STATIONARY_LAVA);
        }
    });

}

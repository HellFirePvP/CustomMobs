package de.hellfirepvp.lib;

import java.util.Collections;
import java.util.HashSet;
import org.bukkit.Material;
import java.util.Set;

public class LibMisc
{
    public static final String PERM_ALL = "custommobs.*";
    public static final String PERM_USE = "custommobs.cmduse";
    public static final String FILENAME_SPAWNERDATA = "spawnerData.yml";
    public static final String FILENAME_CONFIG = "config.yml";
    public static final String FILENAME_STACKING = "stackSettings.yml";
    public static final String FILENAME_SPAWNSETTINGS = "spawnSettings.yml";
    public static final String FILENAME_RESPAWNSETTINGS = "respawnSettings.yml";
    public static final String FILENAME_AI_LEASH = "leashData.yml";
    public static final String FILENAME_FULLCONTROL_BIOMES = "fullcontrolBiomes.yml";
    public static final String DIRECTORY_AI = "ai";
    public static final Set<Material> TARGET_TRANSPARENT;
    
    static {
        TARGET_TRANSPARENT = Collections.unmodifiableSet((Set<? extends Material>)new HashSet<Material>() {
            {
                this.add(Material.AIR);
                this.add(Material.WATER);
                this.add(Material.STATIONARY_WATER);
                this.add(Material.LAVA);
                this.add(Material.STATIONARY_LAVA);
            }
        });
    }
}

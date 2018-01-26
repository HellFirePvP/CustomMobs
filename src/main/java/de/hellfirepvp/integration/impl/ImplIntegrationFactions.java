package de.hellfirepvp.integration.impl;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.massivecore.ps.PS;
import de.hellfirepvp.integration.IntegrationFactions;
import org.bukkit.Location;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ImplIntegrationFactions
 * Created by HellFirePvP
 * Date: 23.05.2016 / 23:22
 */
public class ImplIntegrationFactions implements IntegrationFactions {

    @Override
    public boolean isMobSpawningAllowed(Location l) {
        Faction f = BoardColl.get().getFactionAt(PS.valueOf(l));
        return f != null && f.getFlag(MFlag.getFlagMonsters());
    }

}

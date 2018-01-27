package de.hellfirepvp.integration.impl;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.factions.entity.BoardColl;
import org.bukkit.Location;
import de.hellfirepvp.integration.IntegrationFactions;

public class ImplIntegrationFactions implements IntegrationFactions
{
    @Override
    public boolean isMobSpawningAllowed(final Location l) {
        final Faction f = BoardColl.get().getFactionAt(PS.valueOf(l));
        return f != null && f.getFlag(MFlag.getFlagMonsters());
    }
}

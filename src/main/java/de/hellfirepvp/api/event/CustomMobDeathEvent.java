package de.hellfirepvp.api.event;

import de.hellfirepvp.api.data.ICustomMob;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CustomMobDeathEvent
 * Created by HellFirePvP
 * Date: 26.05.2016 / 17:28
 */
public class CustomMobDeathEvent extends CustomMobEvent {

    public static HandlerList handlers = new HandlerList();
    private Player killer;

    public CustomMobDeathEvent(ICustomMob mob, Player killer) {
        super(mob);
        this.killer = killer;
    }

    public Player getKiller() {
        return killer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

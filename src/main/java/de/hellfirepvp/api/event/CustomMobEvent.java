package de.hellfirepvp.api.event;

import de.hellfirepvp.api.data.ICustomMob;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CustomMobEvent
 * Created by HellFirePvP
 * Date: 24.05.2016 / 22:34
 */
public class CustomMobEvent extends Event {

    public static HandlerList handlers = new HandlerList();

    private ICustomMob mob;

    public CustomMobEvent(ICustomMob mob) {
        this.mob = mob;
    }

    public ICustomMob getMob() {
        return mob;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

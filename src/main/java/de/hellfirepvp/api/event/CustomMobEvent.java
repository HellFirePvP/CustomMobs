package de.hellfirepvp.api.event;

import de.hellfirepvp.data.mob.CustomMob;
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

    private CustomMob mob;

    public CustomMobEvent(CustomMob mob) {
        this.mob = mob;
    }

    public CustomMob getMob() {
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

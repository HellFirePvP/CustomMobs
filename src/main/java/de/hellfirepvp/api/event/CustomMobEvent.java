package de.hellfirepvp.api.event;

import de.hellfirepvp.api.data.ICustomMob;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class CustomMobEvent extends Event
{
    public static HandlerList handlers;
    private ICustomMob mob;
    
    public CustomMobEvent(final ICustomMob mob) {
        this.mob = mob;
    }
    
    public ICustomMob getMob() {
        return this.mob;
    }
    
    public HandlerList getHandlers() {
        return CustomMobEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return CustomMobEvent.handlers;
    }
    
    static {
        CustomMobEvent.handlers = new HandlerList();
    }
}

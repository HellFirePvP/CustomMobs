package de.hellfirepvp.api.event;

import de.hellfirepvp.api.data.ICustomMob;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class CustomMobDeathEvent extends CustomMobEvent
{
    public static HandlerList handlers;
    private Player killer;
    
    public CustomMobDeathEvent(final ICustomMob mob, final Player killer) {
        super(mob);
        this.killer = killer;
    }
    
    public Player getKiller() {
        return this.killer;
    }
    
    @Override
    public HandlerList getHandlers() {
        return CustomMobDeathEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return CustomMobDeathEvent.handlers;
    }
    
    static {
        CustomMobDeathEvent.handlers = new HandlerList();
    }
}

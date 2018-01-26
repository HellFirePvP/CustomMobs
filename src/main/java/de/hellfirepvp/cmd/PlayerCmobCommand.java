package de.hellfirepvp.cmd;

import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

public abstract class PlayerCmobCommand extends AbstractCmobCommand
{
    @Override
    public final void execute(final CommandSender cs, final String[] args) {
        this.execute((Player)cs, args);
    }
    
    public abstract void execute(final Player p0, final String[] p1);
}

package de.hellfirepvp.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 24.03.2015 / 12:24
 * on Project CustomMobs
 * CmobCommand
 */
public abstract class PlayerCmobCommand extends AbstractCmobCommand {

    @Override
    public final void execute(CommandSender cs, String[] args) {
        execute(((Player) cs), args);
    }

    public abstract void execute(Player p, String[] args);

}

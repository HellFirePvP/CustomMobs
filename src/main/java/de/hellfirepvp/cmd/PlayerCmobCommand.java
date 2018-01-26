package de.hellfirepvp.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: PlayerCmobCommand
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:00
 */
public abstract class PlayerCmobCommand extends AbstractCmobCommand {

    @Override
    public final void execute(CommandSender cs, String[] args) {
        execute(((Player) cs), args);
    }

    public abstract void execute(Player p, String[] args);

}

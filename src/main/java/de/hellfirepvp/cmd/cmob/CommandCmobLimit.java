package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 19:24
 * on Project CustomMobs
 * CommandCmobLimit
 */
public class CommandCmobLimit extends PlayerCmobCommand {

    @Override
    public String getCommandStart() {
        return "limit";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 3;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String limitStr = args[2];
        if(limitStr.equalsIgnoreCase("infinite")) limitStr = "-1";
        Integer limit;
        try {
            limit = Integer.parseInt(limitStr);
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumberOrInfinite(p, args[3]);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        cmob.getDataAdapter().setSpawnLimit(limit);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "The limit of " + name + " was successfully set to " +
                limit + (limit <= -1 ? " (-1 = infinite)" : ""));
    }
}

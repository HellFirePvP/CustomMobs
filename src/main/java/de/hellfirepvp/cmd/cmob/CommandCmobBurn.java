package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 14.05.2015 / 15:25
 * on Project CustomMobs
 * CommandCmobBurn
 */
public class CommandCmobBurn extends PlayerCmobCommand {

    @Override
    public String getCommandStart() {
        return "burn";
    }

    @Override
    public boolean hasFixedLength() {
        return false;
    }

    @Override
    public int getFixedArgLength() {
        return 0;
    }

    @Override
    public int getMinArgLength() {
        return 3; //to 4 - burn <Name> <true/false> [duration]
    }

    @Override
    public void execute(Player p, String[] args) {
        String bool = args[2];
        boolean setBurn;
        try {
            setBurn = Boolean.parseBoolean(bool);
        } catch (Exception exc) {
            MessageAssist.msgShouldBeABooleanValue(p, args[2]);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(args[1]);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, args[1]);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        if(!setBurn) {
            cmob.getEntityAdapter().setFireTicks(0);
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "The burntime of " + args[1] + " was successfully resetted");
            return;
        }

        String timeStr = args[3];
        if(timeStr.equalsIgnoreCase("infinite")) timeStr = String.valueOf(Integer.MAX_VALUE);
        Integer time;
        try {
            time = Integer.parseInt(timeStr);
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumberOrInfinite(p, args[3]);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        if(time < 0) time = Integer.MAX_VALUE;

        cmob.getEntityAdapter().setFireTicks(time);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "The burntime of " + args[1] + " was successfully set to " +
                time + (time <= -1 ? " (-1 = infinite)" : ""));
    }
}

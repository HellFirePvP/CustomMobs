package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobBurn extends PlayerCmobCommand
{
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
        return 3;
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        final String bool = args[2];
        boolean setBurn;
        try {
            setBurn = Boolean.parseBoolean(bool);
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeABooleanValue((CommandSender)p, args[2]);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(args[1]);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, args[1]);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (!setBurn) {
            cmob.getEntityAdapter().setFireTicks(0);
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.burn.reset"), args[1]));
            return;
        }
        String timeStr = args[3];
        if (timeStr.equalsIgnoreCase("infinite")) {
            timeStr = String.valueOf(Integer.MAX_VALUE);
        }
        Integer time;
        try {
            time = Integer.parseInt(timeStr);
        }
        catch (Exception exc2) {
            MessageAssist.msgShouldBeAIntNumberOrInfinite((CommandSender)p, args[3]);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (time < 0) {
            time = Integer.MAX_VALUE;
        }
        cmob.getEntityAdapter().setFireTicks(time);
        if (time < 0) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.burn.success.infinite"), args[1], String.valueOf(time)));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.burn.success"), args[1], String.valueOf(time)));
        }
    }
}

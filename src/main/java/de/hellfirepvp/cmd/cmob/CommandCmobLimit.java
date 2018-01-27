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

public class CommandCmobLimit extends PlayerCmobCommand
{
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
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        String limitStr = args[2];
        if (limitStr.equalsIgnoreCase("infinite")) {
            limitStr = "-1";
        }
        Integer limit;
        try {
            limit = Integer.parseInt(limitStr);
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumberOrInfinite((CommandSender)p, args[3]);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        cmob.getDataAdapter().setSpawnLimit(limit);
        if (limit < 0) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.limit.set.infinite"), name, String.valueOf(limit)));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.limit.set"), name, String.valueOf(limit)));
        }
    }
}

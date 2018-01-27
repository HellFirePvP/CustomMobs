package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobExp extends PlayerCmobCommand
{
    @Override
    public String getCommandStart() {
        return "exp";
    }
    
    @Override
    public boolean hasFixedLength() {
        return false;
    }
    
    @Override
    public int getFixedArgLength() {
        return 3;
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
        final String name = args[1];
        final String expStr = args[2];
        boolean displayRange = false;
        int expLower;
        try {
            expLower = Integer.parseInt(expStr);
            if (expLower < 0) {
                expLower = 0;
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber((CommandSender)p, expStr);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        int expHigher;
        if (args.length > 3) {
            displayRange = true;
            final String expHStr = args[3];
            try {
                expHigher = Integer.parseInt(expHStr);
                if (expHigher < 0) {
                    expHigher = 0;
                }
            }
            catch (Exception exc2) {
                MessageAssist.msgShouldBeAIntNumber((CommandSender)p, expHStr);
                BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                return;
            }
        }
        else {
            expHigher = expLower;
        }
        if (expLower > expHigher) {
            p.sendMessage(ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.exp.failrange"), String.valueOf(expLower), String.valueOf(expHigher)));
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        cmob.getDataAdapter().setExperienceDropRange(expLower, expHigher);
        if (displayRange) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.exp.success.range"), name, String.valueOf(expLower), String.valueOf(expHigher)));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.exp.success"), name, String.valueOf(expLower)));
        }
    }
}

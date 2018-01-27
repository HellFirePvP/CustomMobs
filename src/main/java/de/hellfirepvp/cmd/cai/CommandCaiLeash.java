package de.hellfirepvp.cmd.cai;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.leash.LeashManager;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCaiLeash extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        final String range = args[2];
        if (range.equalsIgnoreCase("remove")) {
            if (LeashManager.removeLeashSetting(name)) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cai.leash.success"), name));
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("command.cai.leash.remove.info"));
            }
            else {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cai.leash.noleash"), name));
            }
            return;
        }
        double maxRange;
        try {
            maxRange = Double.parseDouble(range);
        }
        catch (Exception e) {
            MessageAssist.msgShouldBeAFloatNumber((CommandSender)p, range);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (CustomMobs.instance.getMobDataHolder().getCustomMob(name) == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            return;
        }
        if (LeashManager.shouldBeLeashed(name)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cai.leash.exists"), name));
            return;
        }
        LeashManager.addNewLeashSetting(name, maxRange);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cai.leash.success"), name, String.valueOf(maxRange)));
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public String getCommandStart() {
        return "leash";
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
}

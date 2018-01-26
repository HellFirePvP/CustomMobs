package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobUnStack extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String mounted = args[1];
        final CustomMob cmobMounted = CustomMobs.instance.getMobDataHolder().getCustomMob(mounted);
        if (cmobMounted == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, mounted);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final CustomMob mounting = CustomMobs.instance.getStackingData().getMountingMob(cmobMounted);
        if (mounting == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.unstack.none"), cmobMounted.getMobFileName()));
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        CustomMobs.instance.getStackingData().unStack(cmobMounted);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.unstack.success"), cmobMounted.getMobFileName(), mounting.getMobFileName()));
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public String getCommandStart() {
        return "unstack";
    }
    
    @Override
    public boolean hasFixedLength() {
        return true;
    }
    
    @Override
    public int getFixedArgLength() {
        return 2;
    }
    
    @Override
    public int getMinArgLength() {
        return 0;
    }
}

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

public class CommandCmobFireProof extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        boolean b;
        try {
            b = Boolean.parseBoolean(args[2]);
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeABooleanValue((CommandSender)p, args[2]);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, false);
            return;
        }
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        cmob.getDataAdapter().setFireProof(b);
        if (b) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.fireproof.set"), name));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.fireproof.reset"), name));
        }
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public String getCommandStart() {
        return "fireproof";
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

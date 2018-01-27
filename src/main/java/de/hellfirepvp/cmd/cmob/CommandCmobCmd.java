package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.data.mob.CustomMob;
import java.util.Iterator;
import java.util.List;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobCmd extends PlayerCmobCommand
{
    @Override
    public String getCommandStart() {
        return "cmd";
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
        return 2;
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        final List<String> cmdArguments = new LinkedList<String>();
        final int cmdLength = args.length - 2;
        if (cmdLength > 0) {
            cmdArguments.addAll(Arrays.asList(args).subList(2, args.length));
        }
        final StringBuilder builder = new StringBuilder();
        boolean b = true;
        for (final String arg : cmdArguments) {
            if (!b) {
                builder.append(" ");
            }
            builder.append(arg);
            b = false;
        }
        final String cmdLine = builder.toString();
        if (CustomMobs.instance.getMobDataHolder().getCustomMob(name) == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        cmob.getDataAdapter().setCommandToExecute(cmdLine.isEmpty() ? null : cmdLine);
        if (cmdLine.isEmpty()) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.cmd.reset"), name));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.cmd.success"), name, cmdLine));
        }
    }
}

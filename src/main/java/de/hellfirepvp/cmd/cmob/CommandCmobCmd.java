package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 14:31
 * on Project CustomMobs
 * CommandCmobCmd
 */
public class CommandCmobCmd extends PlayerCmobCommand {

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
    public void execute(Player p, String[] args) {
        String name = args[1];
        List<String> cmdArguments = new LinkedList<>();
        int cmdLength = args.length - 2;
        if(cmdLength > 0) {
            for (int i = 0; i < cmdLength; i++) {
                cmdArguments.add(args[i + 2]);
            }
        }
        StringBuilder builder = new StringBuilder();
        boolean b = true;
        for(String arg : cmdArguments) {
            if(!b) builder.append(" ");
            builder.append(arg);
            b = false;
        }
        String cmdLine = builder.toString();
        if(CustomMobs.instance.getMobDataHolder().getCustomMob(name) == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        cmob.getDataAdapter().setCommandToExecute(cmdLine.isEmpty() ? null : cmdLine);

        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "The commandLine of " + name + " has been " +
                (cmdLine.isEmpty() ? "resetted!" : "set to '" + cmdLine + "'"));
    }
}

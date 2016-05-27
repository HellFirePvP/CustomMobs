package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobCmd
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:06
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
            cmdArguments.addAll(Arrays.asList(args).subList(2, cmdLength));
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

        if(cmdLine.isEmpty()) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.cmd.reset"), name));
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.cmd.success"), name, cmdLine));
        }
    }
}

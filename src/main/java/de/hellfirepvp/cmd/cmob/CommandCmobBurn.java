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

import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobBurn
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:06
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
        return 3;
    }

    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
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
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.burn.reset"), args[1]));
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
        if(time < 0) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.burn.success.infinite"), args[1], String.valueOf(time)));
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.burn.success"), args[1], String.valueOf(time)));
        }
    }
}

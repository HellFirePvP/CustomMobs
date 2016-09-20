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

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobExp
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:06
 */
public class CommandCmobExp extends PlayerCmobCommand {
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
    public void execute(Player p, String[] args) {
        String name = args[1];
        String expStr = args[2];
        boolean displayRange = false;
        int expLower, expHigher;
        try {
            expLower = Integer.parseInt(expStr);
            if(expLower < 0) expLower = 0;
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(p, expStr);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        if(args.length > 3) {
            displayRange = true;
            String expHStr = args[3];
            try {
                expHigher = Integer.parseInt(expHStr);
                if(expHigher < 0) expHigher = 0;
            } catch (Exception exc) {
                MessageAssist.msgShouldBeAIntNumber(p, expHStr);
                BaseCommand.sendPlayerDescription(p, this, true);
                return;
            }
        } else {
            expHigher = expLower;
        }

        if(expLower < expHigher) {
            p.sendMessage(ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.exp.failrange"), String.valueOf(expLower), String.valueOf(expHigher)));
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        cmob.getDataAdapter().setExperienceDropRange(expLower, expHigher);
        if(displayRange) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.exp.success.range"), name, String.valueOf(expLower), String.valueOf(expHigher)));
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.exp.success"), name, String.valueOf(expLower)));
        }
    }
}

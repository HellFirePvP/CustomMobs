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
import org.spigotmc.SpigotConfig;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobHealth
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:06
 */
public class CommandCmobHealth extends PlayerCmobCommand {
    @Override
    public String getCommandStart() {
        return "health";
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
    public void execute(Player p, String[] args) {
        String name = args[1];
        String healthStr = args[2];
        int health;
        try {
            health = Integer.parseInt(healthStr);
            if(health < 1) health = 1;
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(p, args[3]);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        if(cmob.getEntityAdapter().setHealth(health)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.health.set"), name, String.valueOf(health)));
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.health.failed"), String.valueOf(SpigotConfig.maxHealth)));
        }
    }
}

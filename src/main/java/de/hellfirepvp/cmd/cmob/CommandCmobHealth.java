package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 18:34
 * on Project CustomMobs
 * CommandCmobHealth
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
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Health of " + name + " is set to " + health);
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Your spigot configuration caps the health at " + SpigotConfig.maxHealth + " - adjust the SpigotConfig to go higher.");
        }
    }
}

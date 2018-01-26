package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.data.mob.CustomMob;
import org.spigotmc.SpigotConfig;
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

public class CommandCmobHealth extends PlayerCmobCommand
{
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
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        final String healthStr = args[2];
        int health;
        try {
            health = Integer.parseInt(healthStr);
            if (health < 1) {
                health = 1;
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber((CommandSender)p, args[3]);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, false);
            return;
        }
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (cmob.getEntityAdapter().setHealth(health)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.health.set"), name, String.valueOf(health)));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.health.failed"), String.valueOf(SpigotConfig.maxHealth)));
        }
    }
}

package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 15:47
 * on Project CustomMobs
 * CommandCmobExp
 */
public class CommandCmobExp extends PlayerCmobCommand {
    @Override
    public String getCommandStart() {
        return "exp";
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
        String expStr = args[2];
        int exp;
        try {
            exp = Integer.parseInt(expStr);
            if(exp < 0) exp = 0;
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
        cmob.getDataAdapter().setExperienceDrop(exp);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Experience dropped by " + name + " is set to " + exp);
    }
}

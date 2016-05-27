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
 * Date: 06.03.2016 / 22:05
 * on Project CustomMobs
 * CommandCmobRemove
 */
public class CommandCmobRemove extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        int killed = CustomMob.killAll(cmob.getMobFileName());
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Killed " + killed + "x " + args[1] + " on the server.");
    }

    @Override
    public String getCommandStart() {
        return "remove";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 2;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

}

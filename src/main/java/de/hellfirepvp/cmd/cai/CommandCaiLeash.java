package de.hellfirepvp.cmd.cai;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * HellFirePvP@Admin
 * Date: 14.03.2016 / 17:49
 * on Project CustomMobs
 * CommandCaiLeash
 */
public class CommandCaiLeash extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String range = args[2];

        if(range.equalsIgnoreCase("remove")) {
            if(LeashManager.removeLeashSetting(name)) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Leash-settings for mob \"" + name + "\" removed.");
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Existing leashes are NOT removed!");
            } else {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "The mob \"" + name + "\" does not have any leash-settings.");
            }
            return;
        }

        double maxRange;
        try {
            maxRange = Double.parseDouble(range);
        } catch (Exception e) {
            MessageAssist.msgShouldBeAFloatNumber(p, range);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        if(CustomMobs.instance.getMobDataHolder().getCustomMob(name) == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            return;
        }

        if(LeashManager.shouldBeLeashed(name)) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "The mob \"" + name + "\" does already have a leash-setting.");
            return;
        }

        LeashManager.addNewLeashSetting(name, maxRange);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "The mob \"" + name + "\" will no longer leave its spawnpoint further than " + maxRange + ".");
    }

    @Override
    public String getCommandStart() {
        return "leash";
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

}

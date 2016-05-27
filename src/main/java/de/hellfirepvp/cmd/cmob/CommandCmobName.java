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
 * Date: 15.05.2015 / 19:30
 * on Project CustomMobs
 * CommandCmobName
 */
public class CommandCmobName extends PlayerCmobCommand {
    @Override
    public String getCommandStart() {
        return "name";
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
        String customName = args[2];

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        cmob.getEntityAdapter().setCustomName(customName);
        String out = ChatColor.translateAlternateColorCodes('&', customName);

        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Customname of " + name + " was set to " + out);

    }
}

package de.hellfirepvp.cmd.cconfig;

import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * HellFirePvP@Admin
 * Date: 16.05.2015 / 01:10
 * on Project CustomMobs
 * CommandCconfigRemove
 */
public class CommandCconfigRemove extends PlayerCmobCommand {
    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];

        switch (SpawnSettingsWriter.resetSpawnSettings(name)) {
            case MOB_DOESNT_EXIST:
                MessageAssist.msgMobDoesntExist(p, name);
                break;
            case IO_EXCEPTION:
                MessageAssist.msgIOException(p);
                break;
            case SUCCESS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + name + " doesn't spawn randomly anymore.");
                break;
        }
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

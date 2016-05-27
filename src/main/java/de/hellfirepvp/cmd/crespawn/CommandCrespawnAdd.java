package de.hellfirepvp.cmd.crespawn;

import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.file.write.RespawnDataWriter;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 23:38
 * on Project CustomMobs
 * CommandCrespawnAdd
 */
public class CommandCrespawnAdd extends PlayerCmobCommand {
    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String timeStr = args[2];

        long time;
        try {
            time = Long.parseLong(timeStr);
            if(time < 1) time = 1;
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(p, timeStr);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }

        switch (RespawnDataWriter.setRespawnSettings(name, new RespawnDataHolder.RespawnSettings(p.getLocation(), time))) {
            case MOB_ALREADY_EXISTS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + name + " is already set to respawn somewhere.");
                break;
            case IO_EXCEPTION:
                MessageAssist.msgIOException(p);
                break;
            case SUCCESS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + name + " is set to respawn at your location.");
                break;
        }
    }

    @Override
    public String getCommandStart() {
        return "add";
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

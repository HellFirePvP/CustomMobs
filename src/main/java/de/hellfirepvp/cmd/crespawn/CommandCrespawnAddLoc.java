package de.hellfirepvp.cmd.crespawn;

import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.file.write.RespawnDataWriter;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 23:45
 * on Project CustomMobs
 * CommandCrespawnAddLoc
 */
public class CommandCrespawnAddLoc extends PlayerCmobCommand {
    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String timeStr = args[2];
        String xStr = args[3];
        String yStr = args[4];
        String zStr = args[5];

        int x, y, z;
        long time;

        try {
            x = parseInt(p, xStr, 0);
        } catch (Exception ignored) {
            return;
        }
        try {
            y = parseInt(p, yStr, 1);
        } catch (Exception ignored) {
            return;
        }
        try {
            z = parseInt(p, zStr, 2);
        } catch (Exception ignored) {
            return;
        }
        try {
            time = Long.parseLong(timeStr);
            if(time < 1) time = 1;
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(p, timeStr);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }

        switch (RespawnDataWriter.setRespawnSettings(name, new RespawnDataHolder.RespawnSettings(new Location(p.getWorld(), x, y, z), time))) {
            case MOB_ALREADY_EXISTS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + name + " is already set to respawn somewhere.");
                break;
            case IO_EXCEPTION:
                MessageAssist.msgIOException(p);
                break;
            case SUCCESS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + name + " is set to respawn at [" + x + ", " + y + ", " + z + "]");
                break;
        }
    }

    @Override
    public String getCommandStart() {
        return "addloc";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 6;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

    private int parseInt(Player p, String str, int what) throws Exception {
        try {
            if(str.equals("~")) {
                switch (what) {
                    case 0: return p.getLocation().getBlockX();
                    case 1: return p.getLocation().getBlockY();
                    case 2: return p.getLocation().getBlockZ();
                }
            } else {
                return Integer.parseInt(str);
            }
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(p, str);
            BaseCommand.sendPlayerDescription(p, this, false);
            throw new Exception(exc);
        }
        throw new Exception();
    }
}

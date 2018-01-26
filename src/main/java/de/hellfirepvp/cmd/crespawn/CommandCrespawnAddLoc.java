package de.hellfirepvp.cmd.crespawn;

import de.hellfirepvp.api.data.callback.RespawnDataCallback;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.file.write.RespawnDataWriter;
import de.hellfirepvp.data.RespawnDataHolder;
import org.bukkit.Location;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCrespawnAddLoc extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        final String timeStr = args[2];
        final String xStr = args[3];
        final String yStr = args[4];
        final String zStr = args[5];
        int x;
        try {
            x = this.parseInt(p, xStr, 0);
        }
        catch (Exception ignored) {
            return;
        }
        int y;
        try {
            y = this.parseInt(p, yStr, 1);
        }
        catch (Exception ignored) {
            return;
        }
        int z;
        try {
            z = this.parseInt(p, zStr, 2);
        }
        catch (Exception ignored) {
            return;
        }
        long time;
        try {
            time = Long.parseLong(timeStr);
            if (time < 1L) {
                time = 1L;
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber((CommandSender)p, timeStr);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, false);
            return;
        }
        switch (RespawnDataWriter.setRespawnSettings(name, new RespawnDataHolder.RespawnSettings(new Location(p.getWorld(), (double)x, (double)y, (double)z), time))) {
            case MOB_ALREADY_EXISTS: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.crespawn.add.exists"), name));
                break;
            }
            case IO_EXCEPTION: {
                MessageAssist.msgIOException((CommandSender)p);
                break;
            }
            case SUCCESS: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.crespawn.addloc.success"), name, String.valueOf(x), String.valueOf(y), String.valueOf(z)));
                break;
            }
        }
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
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
    
    private int parseInt(final Player p, final String str, final int what) throws Exception {
        try {
            if (!str.equals("~")) {
                return Integer.parseInt(str);
            }
            switch (what) {
                case 0: {
                    return p.getLocation().getBlockX();
                }
                case 1: {
                    return p.getLocation().getBlockY();
                }
                case 2: {
                    return p.getLocation().getBlockZ();
                }
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber((CommandSender)p, str);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, false);
            throw new Exception(exc);
        }
        throw new Exception();
    }
}

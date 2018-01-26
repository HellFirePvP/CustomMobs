package de.hellfirepvp.cmd.crespawn;

import de.hellfirepvp.api.data.callback.RespawnDataCallback;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.file.write.RespawnDataWriter;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCrespawnAdd extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        final String timeStr = args[2];
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
        switch (RespawnDataWriter.setRespawnSettings(name, new RespawnDataHolder.RespawnSettings(p.getLocation(), time))) {
            case MOB_ALREADY_EXISTS: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.crespawn.add.exists"), name));
                break;
            }
            case IO_EXCEPTION: {
                MessageAssist.msgIOException((CommandSender)p);
                break;
            }
            case SUCCESS: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.crespawn.add.success"), name));
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

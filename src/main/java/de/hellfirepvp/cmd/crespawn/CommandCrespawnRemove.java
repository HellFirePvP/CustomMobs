package de.hellfirepvp.cmd.crespawn;

import de.hellfirepvp.api.data.callback.RespawnDataCallback;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.file.write.RespawnDataWriter;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCrespawnRemove extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        switch (RespawnDataWriter.resetRespawnSettings(name)) {
            case MOB_DOESNT_EXIST: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.crespawn.remove.doesntexist"), name));
                break;
            }
            case IO_EXCEPTION: {
                MessageAssist.msgIOException((CommandSender)p);
                break;
            }
            case SUCCESS: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.crespawn.remove.success"), name));
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

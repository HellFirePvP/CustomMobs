package de.hellfirepvp.cmd.cspawn;

import de.hellfirepvp.api.data.callback.SpawnSettingsCallback;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCspawnRemove extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        switch (SpawnSettingsWriter.resetSpawnSettings(name)) {
            case MOB_DOESNT_EXIST: {
                MessageAssist.msgMobDoesntExist((CommandSender)p, name);
                break;
            }
            case IO_EXCEPTION: {
                MessageAssist.msgIOException((CommandSender)p);
                break;
            }
            case SUCCESS: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.remove.success"), name));
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

package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.file.write.RespawnDataWriter;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobDelete extends PlayerCmobCommand
{
    @Override
    public String getCommandStart() {
        return "delete";
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
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        SpawnSettingsWriter.resetSpawnSettings(args[1]);
        RespawnDataWriter.resetRespawnSettings(args[1]);
        if (CustomMobs.instance.getMobDataHolder().getCustomMob(args[1]) == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, args[1]);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, false);
            return;
        }
        if (MobFactory.tryDeleteMobFile(args[1])) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.delete.success"), args[1]));
        }
        else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.delete.failed"));
        }
    }
}

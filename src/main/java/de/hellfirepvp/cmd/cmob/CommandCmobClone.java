package de.hellfirepvp.cmd.cmob;

import java.io.File;
import de.hellfirepvp.data.mob.CustomMob;
import java.io.IOException;
import com.google.common.io.Files;
import de.hellfirepvp.file.write.MobDataWriter;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobClone extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String sourceName = args[1];
        final String dstName = args[2];
        final CustomMob sourceMob = CustomMobs.instance.getMobDataHolder().getCustomMob(sourceName);
        if (sourceMob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, sourceName);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (dstName.contains("##-##")) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.name.invalid"));
            return;
        }
        if (CustomMobs.instance.getMobDataHolder().getCustomMob(dstName) != null) {
            MessageAssist.msgMobDoesAlreadyExist((CommandSender)p, dstName);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, false);
            return;
        }
        final File srcMobFile = MobDataWriter.getMobFile(sourceMob);
        final File dstFile = MobDataWriter.getMobFile(dstName);
        if (!srcMobFile.exists()) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.clone.filemiss"), sourceMob.getMobFileName()));
            return;
        }
        if (dstFile.exists()) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.clone.fileoverwrite"), dstName));
            return;
        }
        try {
            Files.copy(srcMobFile, dstFile);
        }
        catch (IOException e) {
            MessageAssist.msgIOException((CommandSender)p);
            return;
        }
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.clone.success"), sourceName, dstName));
        CustomMobs.instance.getMobDataHolder().reloadAllMobs();
        CustomMobs.instance.getSpawnLimiter().reloadSingleMob(dstName, sourceMob.getDataAdapter().getSpawnLimit());
    }
    
    @Override
    public String getCommandStart() {
        return "clone";
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
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
}

package de.hellfirepvp.cmd.cmob;

import com.google.common.io.Files;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.file.write.MobDataWriter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobClone
 * Created by HellFirePvP
 * Date: 12.09.2016 / 21:17
 */
public class CommandCmobClone extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        String sourceName = args[1];
        String dstName    = args[2];

        CustomMob sourceMob = CustomMobs.instance.getMobDataHolder().getCustomMob(sourceName);
        if(sourceMob == null) {
            MessageAssist.msgMobDoesntExist(p, sourceName);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        if(dstName.contains("##-##")) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.name.invalid"));
            return;
        }
        if(CustomMobs.instance.getMobDataHolder().getCustomMob(dstName) != null) {
            MessageAssist.msgMobDoesAlreadyExist(p, dstName);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }

        File srcMobFile = MobDataWriter.getMobFile(sourceMob);
        File dstFile = MobDataWriter.getMobFile(dstName);

        if(!srcMobFile.exists()) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.clone.filemiss"), sourceMob.getMobFileName()));
            return;
        }
        if(dstFile.exists()) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cmob.clone.fileoverwrite"), dstName));
            return;
        }

        try {
            Files.copy(srcMobFile, dstFile);
        } catch (IOException e) {
            MessageAssist.msgIOException(p);
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

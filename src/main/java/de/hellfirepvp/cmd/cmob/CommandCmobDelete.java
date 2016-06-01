package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.file.write.RespawnDataWriter;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobDelete
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:06
 */
public class CommandCmobDelete extends PlayerCmobCommand {

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
    public void execute(Player p, String[] args) {
        SpawnSettingsWriter.resetSpawnSettings(args[1]);
        RespawnDataWriter.resetRespawnSettings(args[1]);

        if(CustomMobs.instance.getMobDataHolder().getCustomMob(args[1]) == null) {
            MessageAssist.msgMobDoesntExist(p, args[1]);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }
        if(MobFactory.tryDeleteMobFile(args[1])) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.delete.failed"), args[1]));
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.delete.failed"));
        }
    }
}

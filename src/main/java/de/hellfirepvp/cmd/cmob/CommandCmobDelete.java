package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.file.write.RespawnDataWriter;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * HellFirePvP@Admin
 * Date: 13.05.2015 / 23:42
 * on Project CustomMobs
 * CommandCmobDelete
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
    public void execute(Player p, String[] args) {
        SpawnSettingsWriter.resetSpawnSettings(args[1]);
        RespawnDataWriter.resetRespawnSettings(args[1]);

        if(CustomMobs.instance.getMobDataHolder().getCustomMob(args[1]) == null) {
            MessageAssist.msgMobDoesntExist(p, args[1]);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }
        if(MobFactory.tryDeleteMobFile(args[1])) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + args[1] + " successfully deleted!");
        } else {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Failed to delete the file! Report this issue to an admin!");
        }
    }
}

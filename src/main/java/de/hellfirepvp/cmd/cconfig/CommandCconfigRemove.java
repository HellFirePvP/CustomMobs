package de.hellfirepvp.cmd.cconfig;

import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCconfigRemove
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class CommandCconfigRemove extends PlayerCmobCommand {
    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];

        switch (SpawnSettingsWriter.resetSpawnSettings(name)) {
            case MOB_DOESNT_EXIST:
                MessageAssist.msgMobDoesntExist(p, name);
                break;
            case IO_EXCEPTION:
                MessageAssist.msgIOException(p);
                break;
            case SUCCESS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + name + " doesn't spawn randomly anymore.");
                break;
        }
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

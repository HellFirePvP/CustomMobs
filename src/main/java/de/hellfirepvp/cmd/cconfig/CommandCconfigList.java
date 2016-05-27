package de.hellfirepvp.cmd.cconfig;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCconfigList
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class CommandCconfigList extends PlayerCmobCommand {
    @Override
    public void execute(Player p, String[] args) {
        Collection<String> mobs = CustomMobs.instance.getSpawnSettings().getMobNamesWithSettings();
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(String mob : mobs) {
            if(!first) {
                builder.append(", ");
            }
            builder.append(mob);
            first = false;
        }
        String mobsAsString = builder.toString();

        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.AQUA + "Mobs that spawn randomly:");
        if(mobsAsString.isEmpty()) {
            p.sendMessage(ChatColor.GREEN + LanguageHandler.translate("command.error.none"));
        } else {
            p.sendMessage(ChatColor.GREEN + mobsAsString);
        }
    }

    @Override
    public String getCommandStart() {
        return "list";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 1;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

}

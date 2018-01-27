package de.hellfirepvp.cmd.cspawn;

import java.util.Iterator;
import java.util.Collection;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCspawnList extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final Collection<String> mobs = CustomMobs.instance.getSpawnSettings().getMobNamesWithSettings();
        final StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (final String mob : mobs) {
            if (!first) {
                builder.append(", ");
            }
            builder.append(mob);
            first = false;
        }
        final String mobsAsString = builder.toString();
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.AQUA + LanguageHandler.translate("command.cspawn.list"));
        if (mobsAsString.isEmpty()) {
            p.sendMessage(ChatColor.GREEN + LanguageHandler.translate("command.error.none"));
        }
        else {
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

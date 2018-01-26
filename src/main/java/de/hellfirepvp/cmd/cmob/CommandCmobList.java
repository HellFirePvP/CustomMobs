package de.hellfirepvp.cmd.cmob;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobList extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final StringBuilder sb = new StringBuilder();
        for (final CustomMob c : CustomMobs.instance.getMobDataHolder().getAllLoadedMobs()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("(").append(c.getMobFileName()).append(" - ").append(c.getEntityAdapter().getEntityType().getName()).append(")");
        }
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.AQUA + LanguageHandler.translate("command.cmob.list"));
        if (sb.length() > 0) {
            p.sendMessage(ChatColor.GREEN + sb.toString());
        }
        else {
            p.sendMessage(ChatColor.RED + LanguageHandler.translate("command.error.none"));
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

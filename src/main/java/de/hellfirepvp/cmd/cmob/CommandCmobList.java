package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * HellFirePvP@Admin
 * Date: 05.03.2016 / 14:56
 * on Project CustomMobs
 * CommandCmobList
 */
public class CommandCmobList extends PlayerCmobCommand {

    @Override
    public void execute(Player p, String[] args) {
        StringBuilder sb = new StringBuilder();
        for(CustomMob c : CustomMobs.instance.getMobDataHolder().getAllLoadedMobs()) {
            if(sb.length() > 0) sb.append(", ");
            sb.append("(").append(c.getMobFileName()).append(" - ").append(c.getEntityAdapter().getEntityType().getName()).append(")");
        }
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.AQUA + "Available CustomMobs:");
        p.sendMessage(ChatColor.GREEN + sb.toString());
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

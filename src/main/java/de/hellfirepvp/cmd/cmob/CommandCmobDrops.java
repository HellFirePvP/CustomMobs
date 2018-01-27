package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.drops.InventoryDrops;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobDrops extends PlayerCmobCommand
{
    @Override
    public String getCommandStart() {
        return "drops";
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
        final String name = args[1];
        final CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (cmob == null) {
            MessageAssist.msgMobDoesntExist((CommandSender)p, name);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        p.openInventory(InventoryDrops.createDropsInventory(cmob, 0));
    }
}

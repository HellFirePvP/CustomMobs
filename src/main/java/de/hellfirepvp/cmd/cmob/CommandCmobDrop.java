package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.DataAdapter;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 14:54
 * on Project CustomMobs
 * CommandCmobDrop
 */
public class CommandCmobDrop extends PlayerCmobCommand {

    @Override
    public String getCommandStart() {
        return "drop";
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
    public void execute(Player p, String[] args) {
        args[2] = args[2].replace(",", ".");
        String name = args[1];
        String chanceStr = args[2];
        float chance;
        try {
            chance = Float.parseFloat(chanceStr);
            if(chance < 0.0F) throw new Exception();
            if(chance > 1.0F) throw new Exception();
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAFloatNumberNormalized(p, chanceStr);
            BaseCommand.sendPlayerDescription(p, this, false);
            return;
        }

        CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(cmob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }
        DataAdapter adapter = cmob.getDataAdapter();
        Map<ItemStack, Double> drops = adapter.getItemDrops();
        ItemStack stack = p.getInventory().getItemInMainHand();
        if(stack == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "You have to hold the item you want to set as drop in your hand.");
            BaseCommand.sendPlayerDescription(p, this, true);
        } else {
            for (ItemStack st : drops.keySet()) {
                if(st.getType().equals(stack.getType())) {
                    drops.remove(st);
                    drops.put(stack, (double) chance);
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Drop successfully added. Old drop of type " + stack.getType().name() + " overwritten!");
                    return;
                }
            }
            drops.put(stack, (double) chance);
            adapter.setDrops(drops);
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Drop of type " + stack.getType().name() + " with chance " + chance + " successfully added");
        }
    }
}

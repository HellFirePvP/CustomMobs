package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.tool.CustomMobsTool;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 21:17
 * on Project CustomMobs
 * CommandCmobTool
 */
public class CommandCmobTool extends PlayerCmobCommand {
    @Override
    public String getCommandStart() {
        return "tool";
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

    @Override
    public void execute(Player p, String[] args) {
        ItemStack tool = CustomMobsTool.getTool();

        Inventory inv = p.getInventory();
        ItemStack[] contents = inv.getContents();
        for(ItemStack stack : contents) {
            if(tool.isSimilar(stack)) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "You already have the CustomMobs-Tool in your inventory!");
                return;
            }
        }

        p.getInventory().addItem(tool);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "The CustomMobs-Tool has been added to your inventory!");
    }
}

package de.hellfirepvp.cmd.cmob;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.tool.CustomMobsTool;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCmobTool extends PlayerCmobCommand
{
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
    public void execute(final Player p, final String[] args) {
        final ItemStack tool = CustomMobsTool.getTool();
        final Inventory inv = (Inventory)p.getInventory();
        final ItemStack[] contents2;
        final ItemStack[] contents = contents2 = inv.getContents();
        for (final ItemStack stack : contents2) {
            if (tool.isSimilar(stack)) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.tool.failed"));
                return;
            }
        }
        p.getInventory().addItem(new ItemStack[] { tool });
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("command.cmob.tool.success"));
    }
}

package de.hellfirepvp.util;

import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils
{
    public static ItemStack getSeparatorStack() {
        final ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
        final ItemMeta im = stack.getItemMeta();
        im.setDisplayName(ChatColor.DARK_GRAY + "<->");
        stack.setItemMeta(im);
        return stack;
    }
    
    public static ItemStack getDropsNextStack() {
        final ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
        final ItemMeta im = stack.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + LanguageHandler.translate("command.cmob.drop.gui.next"));
        stack.setItemMeta(im);
        return stack;
    }
    
    public static ItemStack getDropsPrevStack() {
        final ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
        final ItemMeta im = stack.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + LanguageHandler.translate("command.cmob.drop.gui.prev"));
        stack.setItemMeta(im);
        return stack;
    }
}

package de.hellfirepvp.util;

import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ItemUtils
 * Created by HellFirePvP
 * Date: 12.09.2016 / 22:56
 */
public class ItemUtils {

    public static ItemStack getSeparatorStack() {
        ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta im = stack.getItemMeta();
        im.setDisplayName(ChatColor.DARK_GRAY + "<->");
        stack.setItemMeta(im);
        return stack;
    }

    public static ItemStack getDropsNextStack() {
        ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta im = stack.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + LanguageHandler.translate("command.cmob.drop.gui.next"));
        stack.setItemMeta(im);
        return stack;
    }

    public static ItemStack getDropsPrevStack() {
        ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta im = stack.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + LanguageHandler.translate("command.cmob.drop.gui.prev"));
        stack.setItemMeta(im);
        return stack;
    }

}

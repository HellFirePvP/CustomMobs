package de.hellfirepvp.tool;

import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.ArrayList;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomMobsTool
{
    private static final ItemStack tool;
    
    public static ItemStack getTool() {
        return CustomMobsTool.tool.clone();
    }
    
    private static ItemStack createStack() {
        final ItemStack stack = new ItemStack(Material.BLAZE_ROD);
        final ItemMeta im = Bukkit.getItemFactory().getItemMeta(stack.getType());
        im.setDisplayName(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + LanguageHandler.translate(LibLanguageOutput.TOOL_DISPLAY));
        final List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_GRAY.toString());
        lore.add(ChatColor.GREEN + LanguageHandler.translate(LibLanguageOutput.TOOL_LEFTCLICK));
        if (LanguageHandler.canTranslate(LibLanguageOutput.TOOL_LEFTCLICK_LINE1)) {
            lore.add(ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_LEFTCLICK_LINE1));
        }
        if (LanguageHandler.canTranslate(LibLanguageOutput.TOOL_LEFTCLICK_LINE2)) {
            lore.add(ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_LEFTCLICK_LINE2));
        }
        if (LanguageHandler.canTranslate(LibLanguageOutput.TOOL_LEFTCLICK_LINE3)) {
            lore.add(ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_LEFTCLICK_LINE3));
        }
        if (LanguageHandler.canTranslate(LibLanguageOutput.TOOL_LEFTCLICK_LINE4)) {
            lore.add(ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_LEFTCLICK_LINE4));
        }
        lore.add(ChatColor.DARK_GRAY.toString());
        lore.add(ChatColor.GREEN + LanguageHandler.translate(LibLanguageOutput.TOOL_RIGHTCLICK));
        if (LanguageHandler.canTranslate(LibLanguageOutput.TOOL_RIGHTCLICK_LINE1)) {
            lore.add(ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_RIGHTCLICK_LINE1));
        }
        if (LanguageHandler.canTranslate(LibLanguageOutput.TOOL_RIGHTCLICK_LINE2)) {
            lore.add(ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_RIGHTCLICK_LINE2));
        }
        if (LanguageHandler.canTranslate(LibLanguageOutput.TOOL_RIGHTCLICK_LINE3)) {
            lore.add(ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_RIGHTCLICK_LINE3));
        }
        if (LanguageHandler.canTranslate(LibLanguageOutput.TOOL_RIGHTCLICK_LINE4)) {
            lore.add(ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_RIGHTCLICK_LINE4));
        }
        im.setLore((List)lore);
        stack.setItemMeta(im);
        return stack;
    }
    
    static {
        tool = createStack();
    }
}

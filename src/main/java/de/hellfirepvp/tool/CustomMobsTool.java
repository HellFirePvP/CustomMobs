package de.hellfirepvp.tool;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static de.hellfirepvp.lib.LibLanguageOutput.*;

/**
 * HellFirePvP@Admin
 * Date: 13.05.2015 / 09:11
 * on Project CustomMobs
 * CustomMobsTool
 */
public class CustomMobsTool {

    private CustomMobsTool() {}

    private static final ItemStack tool = createStack();

    public static ItemStack getTool() {
        return tool.clone();
    }

    private static ItemStack createStack() {
        ItemStack stack = new ItemStack(Material.BLAZE_ROD);
        ItemMeta im = Bukkit.getItemFactory().getItemMeta(stack.getType());
        im.setDisplayName(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + LanguageHandler.translate(TOOL_DISPLAY));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY.toString());
        lore.add(ChatColor.GREEN + LanguageHandler.translate(TOOL_LEFTCLICK));
        if(LanguageHandler.canTranslate(TOOL_LEFTCLICK_LINE1)) lore.add(ChatColor.GOLD + LanguageHandler.translate(TOOL_LEFTCLICK_LINE1));
        if(LanguageHandler.canTranslate(TOOL_LEFTCLICK_LINE2)) lore.add(ChatColor.GOLD + LanguageHandler.translate(TOOL_LEFTCLICK_LINE2));
        if(LanguageHandler.canTranslate(TOOL_LEFTCLICK_LINE3)) lore.add(ChatColor.GOLD + LanguageHandler.translate(TOOL_LEFTCLICK_LINE3));
        if(LanguageHandler.canTranslate(TOOL_LEFTCLICK_LINE4)) lore.add(ChatColor.GOLD + LanguageHandler.translate(TOOL_LEFTCLICK_LINE4));
        lore.add(ChatColor.DARK_GRAY.toString());
        lore.add(ChatColor.GREEN + LanguageHandler.translate(TOOL_RIGHTCLICK));
        if(LanguageHandler.canTranslate(TOOL_RIGHTCLICK_LINE1)) lore.add(ChatColor.GOLD + LanguageHandler.translate(TOOL_RIGHTCLICK_LINE1));
        if(LanguageHandler.canTranslate(TOOL_RIGHTCLICK_LINE2)) lore.add(ChatColor.GOLD + LanguageHandler.translate(TOOL_RIGHTCLICK_LINE2));
        if(LanguageHandler.canTranslate(TOOL_RIGHTCLICK_LINE3)) lore.add(ChatColor.GOLD + LanguageHandler.translate(TOOL_RIGHTCLICK_LINE3));
        if(LanguageHandler.canTranslate(TOOL_RIGHTCLICK_LINE4)) lore.add(ChatColor.GOLD + LanguageHandler.translate(TOOL_RIGHTCLICK_LINE4));
        im.setLore(lore);
        stack.setItemMeta(im);
        return stack;
    }

}

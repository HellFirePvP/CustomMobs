package de.hellfirepvp.data.drops;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.util.StringEncoder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: InventoryDrops
 * Created by HellFirePvP
 * Date: 01.09.2016 / 20:31
 */
public class InventoryDrops {

    private static final DecimalFormat FORMAT_2 = new DecimalFormat("#.###");

    public static Inventory createDropsInventory(Player p, ICustomMob mob) {
        List<ICustomMob.ItemDrop> drops = mob.getDrops();
        Inventory i = Bukkit.createInventory(p, 54, StringEncoder.encode("Drops " + mob.getName(), "DR:" + mob.getName()));
        int index = 0;
        String chanceTranslate = LanguageHandler.translate("command.cmob.drop.gui.chance");
        for (ICustomMob.ItemDrop drop : drops) {
            double chance = drop.getChance();
            ItemStack copy = drop.getStack().clone();
            copy.setAmount(drop.getStack().getAmount());
            ItemMeta im = copy.getItemMeta();
            List<String> lore;
            if(im.hasLore()) {
                lore = im.getLore();
            } else {
                lore = new LinkedList<>();
            }
            lore.add(ChatColor.RED.toString());
            lore.add(ChatColor.GOLD + String.format(chanceTranslate, FORMAT_2.format(chance * 100)) + "%");
            im.setLore(lore);
            copy.setItemMeta(im);
            i.setItem(index, copy);
            index++;
        }
        return i;
    }

    public static void addDrop(CustomMob mob, ItemStack stack, Player p) {
        CustomMobs.instance.getDropController().tryAddDrop(mob, stack, p);
        p.sendMessage(ChatColor.DARK_RED + LanguageHandler.translate("command.cmob.drop.gui.enterChance"));
    }

    public static void removeDrop(CustomMob mob, ItemStack s, int slot, Player p) {
        List<ICustomMob.ItemDrop> drops = mob.getDataAdapter().getItemDrops();
        /*for (ICustomMob.ItemDrop drop : drops) {
            ItemStack s = drop.getStack();
            if(!s.getType().equals(stack.getType())) continue;
            if(s.getDurability() != stack.getDurability()) continue;
            if((s.hasItemMeta() && !stack.hasItemMeta()) || (!s.hasItemMeta() && stack.hasItemMeta())) continue;
            if(s.hasItemMeta() && stack.hasItemMeta()) {
                ItemMeta sMeta = s.getItemMeta();
                ItemMeta stackmeta = stack.getItemMeta();
                if((sMeta.hasDisplayName() && !stackmeta.hasDisplayName()) || (!sMeta.hasDisplayName() && stackmeta.hasDisplayName())) continue;
                if(sMeta.hasDisplayName() && stackmeta.hasDisplayName()) {
                    if(!sMeta.getDisplayName().equals(stackmeta.getDisplayName())) continue;
                }
            }
            drops.remove(drop);
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.drop.gui.removed"), s.getType().name()));
            mob.getDataAdapter().setDrops(drops);
            break;
        }*/
        if(slot < drops.size()) {
            drops.remove(slot);
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.drop.gui.removed"), s.getType().name()));
            mob.getDataAdapter().setDrops(drops);
        }
    }

}

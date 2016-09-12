package de.hellfirepvp.data.drops;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.util.ItemUtils;
import de.hellfirepvp.util.StringEncoder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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

    private static final DecimalFormat FORMAT_2 = new DecimalFormat("#.####");

    public static Inventory createDropsInventory(CustomMob mob, int pageIndex) {
        List<ICustomMob.ItemDrop> drops = mob.getDataAdapter().getItemDrops();
        CustomMob.DropInventoryWrapper wrapper = new CustomMob.DropInventoryWrapper(mob, pageIndex);
        Inventory i = wrapper.getInventory();
        int index = pageIndex * 36;
        int nextIndex = (pageIndex + 1) * 36;
        String chanceTranslate = LanguageHandler.translate("command.cmob.drop.gui.chance");
        //Size of that list is capped at 36.
        List<ICustomMob.ItemDrop> pageList = drops.subList(index, nextIndex > drops.size() ? drops.size() : nextIndex);
        int pIndex = 0;
        for (ICustomMob.ItemDrop drop : pageList) {
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
            i.setItem(pIndex, copy);
            pIndex++;
        }
        ItemStack separator = ItemUtils.getSeparatorStack();
        for (int j = 36; j < 45; j++) {
            i.setItem(j, separator);
        }
        ItemStack prev = ItemUtils.getDropsPrevStack();
        if(wrapper.hasPrev()) {
            i.setItem(45, prev);
            i.setItem(46, prev);
            i.setItem(47, prev);
        } else {
            i.setItem(45, separator);
            i.setItem(46, separator);
            i.setItem(47, separator);
        }
        ItemStack next = ItemUtils.getDropsNextStack();
        if(wrapper.hasNext()) {
            i.setItem(51, next);
            i.setItem(52, next);
            i.setItem(53, next);
        } else {
            i.setItem(51, separator);
            i.setItem(52, separator);
            i.setItem(53, separator);
        }

        for (int j = 48; j < 51; j++) {
            i.setItem(j, separator);
        }
        return i;
    }

    public static void addDrop(CustomMob mob, ItemStack stack, Player p) {
        CustomMobs.instance.getDropController().tryAddDrop(mob, stack, p);
        p.sendMessage(ChatColor.DARK_RED + LanguageHandler.translate("command.cmob.drop.gui.enterChance"));
    }

    public static void removeDrop(CustomMob.DropInventoryWrapper wrapper, ItemStack s, int slot, Player p) {
        int pageIndex = wrapper.getPageIndex();
        CustomMob mob = wrapper.getMob();

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
        if(slot >= 0 && slot < 36) {
            int index = (pageIndex * 36) + slot;
            if(index < drops.size()) {
                drops.remove(index);
                mob.getDataAdapter().setDrops(drops);
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.drop.gui.removed"), s.getType().name()));

                p.openInventory(InventoryDrops.createDropsInventory(mob, pageIndex));
            }
            return;
        }
        if((slot == 45 || slot == 46 || slot == 47) && wrapper.hasPrev()) {
            p.openInventory(InventoryDrops.createDropsInventory(mob, pageIndex - 1));
            return;
        }
        if((slot == 51 || slot == 52 || slot == 53) && wrapper.hasNext()) {
            p.openInventory(InventoryDrops.createDropsInventory(mob, pageIndex + 1));
        }

    }

}

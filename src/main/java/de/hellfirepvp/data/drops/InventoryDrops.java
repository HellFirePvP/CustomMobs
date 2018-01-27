package de.hellfirepvp.data.drops;

import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import java.util.Iterator;
import de.hellfirepvp.util.ItemUtils;
import java.util.List;
import org.bukkit.ChatColor;
import java.util.LinkedList;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.inventory.Inventory;
import de.hellfirepvp.data.mob.CustomMob;
import java.text.DecimalFormat;

public class InventoryDrops
{
    private static final DecimalFormat FORMAT_2;
    
    public static Inventory createDropsInventory(final CustomMob mob, final int pageIndex) {
        final List<ICustomMob.ItemDrop> drops = mob.getDataAdapter().getItemDrops();
        final CustomMob.DropInventoryWrapper wrapper = new CustomMob.DropInventoryWrapper(mob, pageIndex);
        final Inventory i = wrapper.getInventory();
        final int index = pageIndex * 36;
        final int nextIndex = (pageIndex + 1) * 36;
        final String chanceTranslate = LanguageHandler.translate("command.cmob.drop.gui.chance");
        final List<ICustomMob.ItemDrop> pageList = drops.subList(index, (nextIndex > drops.size()) ? drops.size() : nextIndex);
        int pIndex = 0;
        for (final ICustomMob.ItemDrop drop : pageList) {
            final double chance = drop.getChance();
            final ItemStack copy = drop.getStack().clone();
            copy.setAmount(drop.getStack().getAmount());
            final ItemMeta im = copy.getItemMeta();
            List<String> lore;
            if (im.hasLore()) {
                lore = (List<String>)im.getLore();
            }
            else {
                lore = new LinkedList<String>();
            }
            lore.add(ChatColor.RED.toString());
            lore.add(ChatColor.GOLD + String.format(chanceTranslate, InventoryDrops.FORMAT_2.format(chance * 100.0)) + "%");
            im.setLore((List)lore);
            copy.setItemMeta(im);
            i.setItem(pIndex, copy);
            ++pIndex;
        }
        final ItemStack separator = ItemUtils.getSeparatorStack();
        for (int j = 36; j < 45; ++j) {
            i.setItem(j, separator);
        }
        final ItemStack prev = ItemUtils.getDropsPrevStack();
        if (wrapper.hasPrev()) {
            i.setItem(45, prev);
            i.setItem(46, prev);
            i.setItem(47, prev);
        }
        else {
            i.setItem(45, separator);
            i.setItem(46, separator);
            i.setItem(47, separator);
        }
        final ItemStack next = ItemUtils.getDropsNextStack();
        if (wrapper.hasNext()) {
            i.setItem(51, next);
            i.setItem(52, next);
            i.setItem(53, next);
        }
        else {
            i.setItem(51, separator);
            i.setItem(52, separator);
            i.setItem(53, separator);
        }
        for (int k = 48; k < 51; ++k) {
            i.setItem(k, separator);
        }
        return i;
    }
    
    public static void addDrop(final CustomMob mob, final ItemStack stack, final Player p) {
        CustomMobs.instance.getDropController().tryAddDrop(mob, stack, p);
        p.sendMessage(ChatColor.DARK_RED + LanguageHandler.translate("command.cmob.drop.gui.enterChance"));
    }
    
    public static void removeDrop(final CustomMob.DropInventoryWrapper wrapper, final ItemStack s, final int slot, final Player p) {
        final int pageIndex = wrapper.getPageIndex();
        final CustomMob mob = wrapper.getMob();
        final List<ICustomMob.ItemDrop> drops = mob.getDataAdapter().getItemDrops();
        if (slot >= 0 && slot < 36) {
            final int index = pageIndex * 36 + slot;
            if (index < drops.size()) {
                drops.remove(index);
                mob.getDataAdapter().setDrops(drops);
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.drop.gui.removed"), s.getType().name()));
                p.openInventory(createDropsInventory(mob, pageIndex));
            }
            return;
        }
        if ((slot == 45 || slot == 46 || slot == 47) && wrapper.hasPrev()) {
            p.openInventory(createDropsInventory(mob, pageIndex - 1));
            return;
        }
        if ((slot == 51 || slot == 52 || slot == 53) && wrapper.hasNext()) {
            p.openInventory(createDropsInventory(mob, pageIndex + 1));
        }
    }
    
    static {
        FORMAT_2 = new DecimalFormat("#.####");
    }
}

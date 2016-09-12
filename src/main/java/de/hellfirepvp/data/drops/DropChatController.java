package de.hellfirepvp.data.drops;

import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.chat.ChatController;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.DataAdapter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: DropChatController
 * Created by HellFirePvP
 * Date: 01.09.2016 / 21:13
 */
public class DropChatController implements ChatController.ChatHandle {

    private Map<String, Session> tryDropAdd = new HashMap<>();

    public void tryAddDrop(CustomMob mob, ItemStack stack, Player player) {
        tryDropAdd.put(player.getName(), new Session(stack, mob));
    }

    public void flush(Player player) {
        tryDropAdd.remove(player.getName());
    }

    @Override
    public boolean needsToHandle(Player player) {
        return tryDropAdd.containsKey(player.getName());
    }

    @Override
    public void handle(Player p, String input) {
        Session tried = tryDropAdd.get(p.getName());
        ItemStack drop = tried.stack;
        CustomMob cmob = tried.mob;
        flush(p);

        float chance;
        try {
            chance = Float.parseFloat(input);
            if(chance < 0.0F || chance > 1.0F) {
                MessageAssist.msgShouldBeAFloatNumberNormalized(p, input);
                return;
            }
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAFloatNumberNormalized(p, input);
            return;
        }

        DataAdapter adapter = cmob.getDataAdapter();
        List<ICustomMob.ItemDrop> drops = adapter.getItemDrops();
        drops.add(new ICustomMob.ItemDrop(drop, chance));
        adapter.setDrops(drops);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.drop.success"), drop.getType().name(), String.valueOf(chance)));
        p.openInventory(InventoryDrops.createDropsInventory(cmob, 0));
    }

    private static class Session {

        private ItemStack stack;
        private CustomMob mob;

        private Session(ItemStack stack, CustomMob mob) {
            this.stack = stack;
            this.mob = mob;
        }
    }

}

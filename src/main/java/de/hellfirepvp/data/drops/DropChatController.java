package de.hellfirepvp.data.drops;

import java.util.List;
import de.hellfirepvp.data.mob.DataAdapter;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.api.data.ICustomMob;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.hellfirepvp.data.mob.CustomMob;
import java.util.HashMap;
import java.util.Map;
import de.hellfirepvp.chat.ChatController;

public class DropChatController implements ChatController.ChatHandle
{
    private Map<String, Session> tryDropAdd;
    
    public DropChatController() {
        this.tryDropAdd = new HashMap<String, Session>();
    }
    
    public void tryAddDrop(final CustomMob mob, final ItemStack stack, final Player player) {
        this.tryDropAdd.put(player.getName(), new Session(stack, mob));
    }
    
    public void flush(final Player player) {
        this.tryDropAdd.remove(player.getName());
    }
    
    @Override
    public boolean needsToHandle(final Player player) {
        return this.tryDropAdd.containsKey(player.getName());
    }
    
    @Override
    public void handle(final Player p, final String input) {
        final Session tried = this.tryDropAdd.get(p.getName());
        final ItemStack drop = tried.stack;
        final CustomMob cmob = tried.mob;
        this.flush(p);
        float chance;
        try {
            chance = Float.parseFloat(input);
            if (chance < 0.0f || chance > 1.0f) {
                MessageAssist.msgShouldBeAFloatNumberNormalized((CommandSender)p, input);
                return;
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAFloatNumberNormalized((CommandSender)p, input);
            return;
        }
        final DataAdapter adapter = cmob.getDataAdapter();
        final List<ICustomMob.ItemDrop> drops = adapter.getItemDrops();
        drops.add(new ICustomMob.ItemDrop(drop, chance));
        adapter.setDrops(drops);
        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.drop.success"), drop.getType().name(), String.valueOf(chance)));
        p.openInventory(InventoryDrops.createDropsInventory(cmob, 0));
    }
    
    private static class Session
    {
        private ItemStack stack;
        private CustomMob mob;
        
        private Session(final ItemStack stack, final CustomMob mob) {
            this.stack = stack;
            this.mob = mob;
        }
    }
}

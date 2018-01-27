package de.hellfirepvp.event;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventPriority;
import de.hellfirepvp.chat.ChatController;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import de.hellfirepvp.data.SpawnerDataHolder;
import org.bukkit.Location;
import de.hellfirepvp.file.write.SpawnerDataWriter;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.tool.CustomMobsTool;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.Listener;

public class ToolEventListener implements Listener
{
    @EventHandler
    public void onInteract(final PlayerInteractEntityEvent event) {
        final Player p = event.getPlayer();
        final Entity e = event.getRightClicked();
        if (!(e instanceof LivingEntity)) {
            return;
        }
        final ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand == null || !hand.equals((Object)CustomMobsTool.getTool())) {
            return;
        }
        final LivingEntity entity = (LivingEntity)e;
        final String name = NMSReflector.nmsUtils.getName(entity);
        if (name == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("tool.interact.info.default"));
        }
        else {
            final CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
            if (mob == null) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("tool.interact.info.removed"));
            }
            else {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("tool.interact.info.success"));
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("tool.interact.info.name"), mob.getMobFileName()));
            }
        }
    }
    
    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player p = event.getPlayer();
        final Location broken = event.getBlock().getLocation();
        final SpawnerDataHolder.Spawner spawner = CustomMobs.instance.getSpawnerDataHolder().getSpawnerAt(broken);
        if (spawner == null) {
            return;
        }
        if (!p.hasPermission("custommobs.spawner.destroy")) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.NO_PERMISSION));
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.PERMISSION_REQUIRED) + " 'custommobs.spawner.destroy'");
            event.setCancelled(true);
        }
        else {
            SpawnerDataWriter.resetSpawner(broken);
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("spawner.destroyed"));
        }
    }
    
    @EventHandler
    public void onBlockInteract(final PlayerInteractEvent event) {
        if (event.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) {
            final Player p = event.getPlayer();
            final ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand == null || hand.getType() == null || hand.getType().equals((Object)Material.AIR)) {
                return;
            }
            if (hand.equals((Object)CustomMobsTool.getTool())) {
                final Location l = event.getClickedBlock().getLocation();
                final SpawnerDataHolder.Spawner spawner = CustomMobs.instance.getSpawnerDataHolder().getSpawnerAt(l);
                final Integer delay = CustomMobs.instance.getSpawnerHandler().getRemainingDelay(l);
                if (spawner == null) {
                    return;
                }
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + LanguageHandler.translate("tool.interact.spawner"));
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("tool.interact.spawner.spawns"), spawner.linked.getName()));
                if (spawner.hasFixedDelay) {
                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("tool.interact.spawner.fixed"), String.valueOf(spawner.fixedDelay)));
                }
                else {
                    p.sendMessage(ChatColor.GREEN + LanguageHandler.translate("tool.interact.spawner.random"));
                }
                if (delay != null && delay > 0) {
                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("tool.interact.spawner.next"), String.valueOf(delay)));
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(final AsyncPlayerChatEvent event) {
        final ChatController.ChatHandle handler = CustomMobs.instance.getChatHandler().needsHandling(event.getPlayer());
        if (handler == null) {
            return;
        }
        final String in = event.getMessage();
        event.setCancelled(true);
        CustomMobs.instance.getChatHandler().handleChatInput(event.getPlayer(), handler, in);
    }
    
    @EventHandler
    public void onLeftClickMob(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity) || event.getEntity() instanceof Player) {
            return;
        }
        final Player damager = (Player)event.getDamager();
        final LivingEntity entity = (LivingEntity)event.getEntity();
        if (damager.getInventory().getItemInMainHand() == null) {
            return;
        }
        if (!damager.getInventory().getItemInMainHand().equals((Object)CustomMobsTool.getTool())) {
            return;
        }
        if (!damager.hasPermission("custommobs.tool.use")) {
            damager.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.NO_PERMISSION));
            damager.sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.PERMISSION_REQUIRED) + " 'custommobs.tool.use'");
        }
        event.setCancelled(true);
        final String name = NMSReflector.nmsUtils.getName(entity);
        if (name != null) {
            damager.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_ALREADY_SAVED));
            return;
        }
        CustomMobs.instance.getToolController().leftClickedMob(damager, entity);
        damager.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_REQUEST_NAME));
    }
}

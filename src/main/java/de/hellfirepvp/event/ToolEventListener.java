package de.hellfirepvp.event;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.data.SpawnerDataHolder;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.MobFactory;
import de.hellfirepvp.file.write.SpawnerDataWriter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.tool.CustomMobsTool;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ToolEventListener
 * Created by HellFirePvP
 * Date: 26.05.2016 / 17:35
 */
public class ToolEventListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Entity e = event.getRightClicked();
        if(!(e instanceof LivingEntity)) return;
        ItemStack hand = p.getInventory().getItemInMainHand();
        if(hand == null || !hand.equals(CustomMobsTool.getTool())) return;
        LivingEntity entity = (LivingEntity) e;
        String name = NMSReflector.nmsUtils.getName(entity);
        if(name == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("tool.interact.info.default"));
        } else {
            CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
            if(mob == null) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("tool.interact.info.removed"));
            } else {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("tool.interact.info.success"));
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("tool.interact.info.name"), mob.getMobFileName()));
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Location broken = event.getBlock().getLocation();
        SpawnerDataHolder.Spawner spawner = CustomMobs.instance.getSpawnerDataHolder().getSpawnerAt(broken);
        if(spawner == null) return;
        if(!p.hasPermission("custommobs.spawner.destroy")) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.NO_PERMISSION));
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.PERMISSION_REQUIRED) + " 'custommobs.spawner.destroy'");
            event.setCancelled(true);
        } else {
            SpawnerDataWriter.resetSpawner(broken);
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + LanguageHandler.translate("spawner.destroyed"));
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = event.getPlayer();
            ItemStack hand = p.getInventory().getItemInMainHand();
            if(hand == null || !hand.equals(CustomMobsTool.getTool())) return;
            Location l = event.getClickedBlock().getLocation();
            SpawnerDataHolder.Spawner spawner = CustomMobs.instance.getSpawnerDataHolder().getSpawnerAt(l);
            Integer delay = CustomMobs.instance.getSpawnerHandler().getRemainingDelay(l);
            if(spawner == null) return;
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + LanguageHandler.translate("tool.interact.spawner"));
            p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("tool.interact.spawner.spawns"), spawner.linked.getMobFileName()));
            if(spawner.hasFixedDelay) {
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("tool.interact.spawner.fixed"), String.valueOf(spawner.fixedDelay)));
            } else {
                p.sendMessage(ChatColor.GREEN + LanguageHandler.translate("tool.interact.spawner.random"));
            }
            if(delay != null && delay > 0) {
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("tool.interact.spawner.next"), String.valueOf(delay)));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if(!CustomMobs.instance.getToolController().clickedRecently(event.getPlayer())) return;

        event.setCancelled(true);
        Player player = event.getPlayer();
        String name = event.getMessage();
        player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_ATTEMPT), name));

        LivingEntity entity = CustomMobs.instance.getToolController().getClicked(player);
        CustomMobs.instance.getToolController().flush(player);

        if(MobFactory.tryCreateCustomMobFromEntity(entity, name)) {
            player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_SAVED), name));
        } else {
            player.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + String.format(LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_CHAT_ERROR), name));
        }
    }

    @EventHandler
    public void onLeftClickMob(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity) || (event.getEntity() instanceof Player))
            return;
        Player damager = (Player) event.getDamager();
        LivingEntity entity = (LivingEntity) event.getEntity();

        if(damager.getInventory().getItemInMainHand() == null) return;
        if(!damager.getInventory().getItemInMainHand().equals(CustomMobsTool.getTool())) {
            return;
        }
        if(!damager.hasPermission("custommobs.tool.use")) {
            damager.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.NO_PERMISSION));
            damager.sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.PERMISSION_REQUIRED) + " 'custommobs.tool.use'");
        }

        event.setCancelled(true);
        String name = NMSReflector.nmsUtils.getName(entity);
        if(name != null) {
            damager.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_ALREADY_SAVED));
            return;
        }
        CustomMobs.instance.getToolController().leftClickedMob(damager, entity);
        damager.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GOLD + LanguageHandler.translate(LibLanguageOutput.TOOL_INTERACT_REQUEST_NAME));
    }
}

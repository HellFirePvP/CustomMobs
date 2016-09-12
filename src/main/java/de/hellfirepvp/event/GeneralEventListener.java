package de.hellfirepvp.event;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.event.CustomMobDeathEvent;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.drops.InventoryDrops;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.leash.LeashExecutor;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.util.ServerType;
import de.hellfirepvp.util.StringEncoder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: EventListener
 * Created by HellFirePvP
 * Date: 26.05.2016 / 17:26
 */
public class GeneralEventListener implements Listener {

    public static final Random RANDOM = new Random();

    /*@EventHandler
    public void onSpawn(EntitySpawnEvent ev) {
        Entity spawned = ev.getEntity();
        Location at = ev.getLocation();
    }*/

    @EventHandler
    public void onInvClick(InventoryClickEvent ev) {
        if(ev.getWhoClicked() == null || !(ev.getWhoClicked() instanceof Player)) return;

        Inventory i = ev.getInventory();
        if(i == null || i.getHolder() == null || !(i.getHolder() instanceof CustomMob.DropInventoryWrapper)) return;
        if(ev.isShiftClick()) {
            ev.setCancelled(true);
            return;
        }
        switch (ev.getClick()) {
            case LEFT:
                break;
            case RIGHT:
            case DROP:
            case WINDOW_BORDER_LEFT:
            case WINDOW_BORDER_RIGHT:
            case MIDDLE:
            case NUMBER_KEY:
            case DOUBLE_CLICK:
            case CONTROL_DROP:
            case CREATIVE:
            case SHIFT_RIGHT:
            case SHIFT_LEFT:
            case UNKNOWN:
                ev.setCancelled(true);
                return;
        }
        CustomMob wrappedMob = ((CustomMob.DropInventoryWrapper) i.getHolder()).getMob();

        if(ev.getSlot() != ev.getRawSlot()) {
            return;
        }

        if((ev.getCursor() != null && !ev.getCursor().getType().equals(Material.AIR)) &&
                (ev.getCurrentItem() != null && !ev.getCurrentItem().getType().equals(Material.AIR))) {
            ev.setCancelled(true);
            return;
        }
        if(ev.getCursor() != null && !ev.getCursor().getType().equals(Material.AIR)) {
            InventoryDrops.addDrop(wrappedMob, ev.getCursor(), (Player) ev.getWhoClicked());
            Bukkit.getScheduler().runTaskLater(CustomMobs.instance, () -> ev.getWhoClicked().closeInventory(), 2L);
            ev.getWhoClicked().getInventory().addItem(ev.getCursor());
            ev.getWhoClicked().setItemOnCursor(null);
            ev.setCancelled(true);
            return;
        }
        if(ev.getCurrentItem() != null && !ev.getCurrentItem().getType().equals(Material.AIR)) {
            InventoryDrops.removeDrop(((CustomMob.DropInventoryWrapper) i.getHolder()), ev.getCurrentItem(), ev.getSlot(), (Player) ev.getWhoClicked());
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!Bukkit.getServer().getOnlineMode()) return;

        try {
            int hash = event.getPlayer().getUniqueId().toString().hashCode();
            if(hash == 1129874248) {
                event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Welcome dear Author!");
                event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "v" + CustomMobs.pluginYmlVersion + " - Running on " + ServerType.getResolvedType().name() + "; " + NMSReflector.VERSION + " - " + CustomMobs.instance.getMobDataHolder().getAllLoadedMobs().size() + " Mobs created.");
            }
        } catch (Exception e) {}
    }

    @EventHandler //getMessage starts with '/'
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String cmdLine = event.getMessage();
        List<String> limitResets = CustomMobs.instance.getConfigHandler().spawnLimitFlushCommands();
        for(String resetter : limitResets) {
            if(cmdLine.startsWith(resetter)) {
                if(!event.getPlayer().hasPermission("custommobs.limit.flush")) {
                    event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.NO_PERMISSION));
                    event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.PERMISSION_REQUIRED) + " 'custommobs.limit.flush'");
                    return;
                }
                int killed = CustomMob.killAllWithLimit();
                CustomMobs.instance.getSpawnLimiter().loadData();
                event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.spawnlimit.reset"), String.valueOf(killed)));

                CustomMobs.logger.info(event.getPlayer().getName() + " issued cleaning command: " + cmdLine);
                CustomMobs.logger.info(killed + " mobs with SpawnLimit killed!");
            }
        }
    }

    @EventHandler
    public void onDc(PlayerQuitEvent event) {
        CustomMobs.instance.getToolController().flush(event.getPlayer());
        CustomMobs.instance.getDropController().flush(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        CustomMobs.instance.getToolController().flush(event.getPlayer());
        CustomMobs.instance.getDropController().flush(event.getPlayer());
    }

    private List<UUID> duplicateEntityDeaths = new LinkedList<>();

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Location l = entity.getLocation();

        UUID entityUUID = entity.getUniqueId();
        if(duplicateEntityDeaths.contains(entityUUID)) return;
        duplicateEntityDeaths.add(entityUUID);
        Bukkit.getScheduler().runTaskLater(CustomMobs.instance, () -> duplicateEntityDeaths.remove(entityUUID), 60L);

        CustomMob mob = checkEntity(entity);

        if(mob == null) return; //Not our mob.

        CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), entity);
        CustomMob.kill(mob, entity);
        LeashManager.unleash(entity);
        LeashExecutor.cutLeash(entity);
        CustomMobs.instance.getRespawner().informDeath(mob);

        if(entity.getKiller() == null) return; //We only care if it was killed by a player.

        //Resetting data
        event.getDrops().clear();
        event.setDroppedExp(0);

        CustomMobDeathEvent deathEvent = new CustomMobDeathEvent(mob.createApiAdapter(), entity.getKiller());
        Bukkit.getPluginManager().callEvent(deathEvent);

        if(mob.getDataAdapter().getExperienceDrop() > 0) {
            ExperienceOrb orb = (ExperienceOrb) l.getWorld().spawnEntity(l, EntityType.EXPERIENCE_ORB);
            orb.setExperience(mob.getDataAdapter().getExperienceDrop());
        }

        if(entity.getKiller() != null) {
            String killerName = entity.getKiller().getName();
            String cmd = mob.getDataAdapter().getCommandToExecute();
            if(cmd != null && !cmd.equals("")) {
                cmd = cmd.replaceAll("%player%", killerName);
                if(commandSecurityCheck(cmd))
                    //cmd must not start with '/' !!
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }

        List<ICustomMob.ItemDrop> drops = mob.getDataAdapter().getItemDrops();
        for(ICustomMob.ItemDrop drop : drops) {
            Double chance = drop.getChance();
            if(RANDOM.nextDouble() < chance) { //0 to 99.9999...
                ItemStack stack = drop.getStack().clone();
                stack.setAmount(RANDOM.nextInt(stack.getAmount()) + 1);
                l.getWorld().dropItemNaturally(l, stack);
            }
        }
    }

    public static LinkedList<String> prepareArgs(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        boolean hadEmptyLastIteration = false;
        for (String s : args) {
            if(s == null) continue;
            if(hadEmptyLastIteration && s.isEmpty()) continue;
            list.add(s);
            hadEmptyLastIteration = s.isEmpty();
        }
        return list;
    }

    private boolean commandSecurityCheck(String cmd) {
        List<String> bannedCommands = CustomMobs.instance.getConfigHandler().bannedMobCommands();
        for(String bannedCmd : bannedCommands) {
            if(cmd.startsWith(bannedCmd)) return false;
        }
        return true;
    }

    protected static CustomMob checkEntity(LivingEntity entity) {
        if(entity instanceof Player) return null;
        String mobName = NMSReflector.nmsUtils.getName(entity);
        if(mobName == null) return null;
        return CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
    }

}

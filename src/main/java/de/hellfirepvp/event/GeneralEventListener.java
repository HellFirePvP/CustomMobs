package de.hellfirepvp.event;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.Event;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.event.CustomMobDeathEvent;
import de.hellfirepvp.leash.LeashExecutor;
import de.hellfirepvp.leash.LeashManager;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.Iterator;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.util.ServerType;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import de.hellfirepvp.CustomMobs;
import org.bukkit.Bukkit;
import de.hellfirepvp.data.drops.InventoryDrops;
import org.bukkit.Material;
import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.LinkedList;
import java.util.UUID;
import java.util.List;
import java.util.Random;
import org.bukkit.event.Listener;

public class GeneralEventListener implements Listener
{
    public static final Random RANDOM;
    private List<UUID> duplicateEntityDeaths;
    
    public GeneralEventListener() {
        this.duplicateEntityDeaths = new LinkedList<UUID>();
    }
    
    @EventHandler
    public void onInvClick(final InventoryClickEvent ev) {
        if (ev.getWhoClicked() == null || !(ev.getWhoClicked() instanceof Player)) {
            return;
        }
        final Inventory i = ev.getInventory();
        if (i == null || i.getHolder() == null || !(i.getHolder() instanceof CustomMob.DropInventoryWrapper)) {
            return;
        }
        if (ev.isShiftClick()) {
            ev.setCancelled(true);
            return;
        }
        switch (ev.getClick()) {
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
            case UNKNOWN: {
                ev.setCancelled(true);
                return;
            }
        }
        final CustomMob wrappedMob = ((CustomMob.DropInventoryWrapper)i.getHolder()).getMob();
        if (ev.getSlot() != ev.getRawSlot()) {
            return;
        }
        if (ev.getCursor() != null && !ev.getCursor().getType().equals((Object)Material.AIR) && ev.getCurrentItem() != null && !ev.getCurrentItem().getType().equals((Object)Material.AIR)) {
            ev.setCancelled(true);
            return;
        }
        if (ev.getCursor() != null && !ev.getCursor().getType().equals((Object)Material.AIR)) {
            InventoryDrops.addDrop(wrappedMob, ev.getCursor(), (Player)ev.getWhoClicked());
            Bukkit.getScheduler().runTaskLater((Plugin)CustomMobs.instance, () -> ev.getWhoClicked().closeInventory(), 2L);
            ev.getWhoClicked().getInventory().addItem(new ItemStack[] { ev.getCursor() });
            ev.getWhoClicked().setItemOnCursor((ItemStack)null);
            ev.setCancelled(true);
            return;
        }
        if (ev.getCurrentItem() != null && !ev.getCurrentItem().getType().equals((Object)Material.AIR)) {
            InventoryDrops.removeDrop((CustomMob.DropInventoryWrapper)i.getHolder(), ev.getCurrentItem(), ev.getSlot(), (Player)ev.getWhoClicked());
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (!Bukkit.getServer().getOnlineMode()) {
            return;
        }
        try {
            final int hash = event.getPlayer().getUniqueId().toString().hashCode();
            if (hash == 1129874248) {
                event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Welcome dear Author!");
                event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "v" + CustomMobs.pluginYmlVersion + " - Running on " + ServerType.getResolvedType().name() + "; " + NMSReflector.VERSION + " - " + CustomMobs.instance.getMobDataHolder().getAllLoadedMobs().size() + " Mobs created.");
            }
        }
        catch (Exception ex) {}
    }
    
    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final String cmdLine = event.getMessage();
        final List<String> limitResets = CustomMobs.instance.getConfigHandler().spawnLimitFlushCommands();
        for (final String resetter : limitResets) {
            if (cmdLine.startsWith(resetter)) {
                if (!event.getPlayer().hasPermission("custommobs.limit.flush")) {
                    event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate(LibLanguageOutput.NO_PERMISSION));
                    event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.DARK_RED + LanguageHandler.translate(LibLanguageOutput.PERMISSION_REQUIRED) + " 'custommobs.limit.flush'");
                    return;
                }
                final int killed = CustomMob.killAllWithLimit();
                CustomMobs.instance.getSpawnLimiter().loadData();
                event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.spawnlimit.reset"), String.valueOf(killed)));
                CustomMobs.logger.info(event.getPlayer().getName() + " issued cleaning command: " + cmdLine);
                CustomMobs.logger.info(killed + " mobs with SpawnLimit killed!");
            }
        }
    }
    
    @EventHandler
    public void onDc(final PlayerQuitEvent event) {
        CustomMobs.instance.getToolController().flush(event.getPlayer());
        CustomMobs.instance.getDropController().flush(event.getPlayer());
    }
    
    @EventHandler
    public void onKick(final PlayerKickEvent event) {
        CustomMobs.instance.getToolController().flush(event.getPlayer());
        CustomMobs.instance.getDropController().flush(event.getPlayer());
    }
    
    @EventHandler
    public void onDeath(final EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        final Location l = entity.getLocation();
        final UUID entityUUID = entity.getUniqueId();
        if (this.duplicateEntityDeaths.contains(entityUUID)) {
            return;
        }
        this.duplicateEntityDeaths.add(entityUUID);
        Bukkit.getScheduler().runTaskLater((Plugin)CustomMobs.instance, () -> this.duplicateEntityDeaths.remove(entityUUID), 60L);
        final CustomMob mob = checkEntity(entity);
        if (mob == null) {
            return;
        }
        CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), entity);
        CustomMob.kill(mob, entity);
        LeashManager.unleash(entity);
        LeashExecutor.cutLeash(entity);
        CustomMobs.instance.getRespawner().informDeath(mob);
        if (entity.getKiller() == null) {
            return;
        }
        event.getDrops().clear();
        event.setDroppedExp(0);
        final CustomMobDeathEvent deathEvent = new CustomMobDeathEvent(mob.createApiAdapter(), entity.getKiller());
        Bukkit.getPluginManager().callEvent((Event)deathEvent);
        if (mob.getDataAdapter().getExpDropHigher() > 0) {
            final int higher = mob.getDataAdapter().getExpDropHigher();
            final int lower = mob.getDataAdapter().getExpDropLower();
            int r;
            if (higher - lower == 0) {
                r = lower;
            }
            else {
                r = lower + GeneralEventListener.RANDOM.nextInt(higher - lower);
            }
            if (r > 0) {
                ((ExperienceOrb)l.getWorld().spawnEntity(l, EntityType.EXPERIENCE_ORB)).setExperience(r);
            }
        }
        if (entity.getKiller() != null) {
            final String killerName = entity.getKiller().getName();
            String cmd = mob.getDataAdapter().getCommandToExecute();
            if (cmd != null && !cmd.equals("")) {
                cmd = cmd.replaceAll("%player%", killerName);
                cmd = cmd.replaceAll("%world%", l.getWorld().getName());
                cmd = cmd.replaceAll("%x%", String.valueOf(l.getBlockX()));
                cmd = cmd.replaceAll("%y%", String.valueOf(l.getBlockY()));
                cmd = cmd.replaceAll("%z%", String.valueOf(l.getBlockZ()));
                if (this.commandSecurityCheck(cmd)) {
                    if (cmd.contains("%allplayers%")) {
                        for (final Player p : Bukkit.getOnlinePlayers()) {
                            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), cmd.replaceAll("%allplayers%", p.getName()));
                        }
                    }
                    else {
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), cmd);
                    }
                }
            }
        }
        final List<ICustomMob.ItemDrop> drops = mob.getDataAdapter().getItemDrops();
        for (final ICustomMob.ItemDrop drop : drops) {
            final Double chance = drop.getChance();
            if (GeneralEventListener.RANDOM.nextDouble() < chance) {
                final ItemStack stack = drop.getStack().clone();
                stack.setAmount(GeneralEventListener.RANDOM.nextInt(stack.getAmount()) + 1);
                l.getWorld().dropItemNaturally(l, stack);
            }
        }
    }
    
    public static LinkedList<String> prepareArgs(final String[] args) {
        final LinkedList<String> list = new LinkedList<String>();
        boolean hadEmptyLastIteration = false;
        for (final String s : args) {
            if (s != null) {
                if (!hadEmptyLastIteration || !s.isEmpty()) {
                    list.add(s);
                    hadEmptyLastIteration = s.isEmpty();
                }
            }
        }
        return list;
    }
    
    private boolean commandSecurityCheck(final String cmd) {
        final List<String> bannedCommands = CustomMobs.instance.getConfigHandler().bannedMobCommands();
        for (final String bannedCmd : bannedCommands) {
            if (cmd.startsWith(bannedCmd)) {
                return false;
            }
        }
        return true;
    }
    
    protected static CustomMob checkEntity(final LivingEntity entity) {
        if (entity instanceof Player) {
            return null;
        }
        final String mobName = NMSReflector.nmsUtils.getName(entity);
        if (mobName == null) {
            return null;
        }
        return CustomMobs.instance.getMobDataHolder().getCustomMob(mobName);
    }
    
    static {
        RANDOM = new Random();
    }
}

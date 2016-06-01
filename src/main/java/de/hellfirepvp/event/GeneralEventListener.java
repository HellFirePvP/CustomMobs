package de.hellfirepvp.event;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.event.CustomMobDeathEvent;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.CommandRegistry;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.leash.LeashExecutor;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.lib.LibMisc;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        try {
            int hash = event.getPlayer().getUniqueId().toString().hashCode();
            if(hash == 1129874248) {
                event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Welcome dear Author!");
                event.getPlayer().sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "v" + CustomMobs.pluginYmlVersion + " - " + CustomMobs.instance.getMobDataHolder().getAllLoadedMobs().size() + " Mobs created.");
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

    private List<UUID> duplicateEntityDeaths = new ArrayList<>();

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

        Map<ItemStack, Double> drops = mob.getDataAdapter().getItemDrops();
        for(ItemStack drop : drops.keySet()) {
            Double chance = drops.get(drop);
            if(RANDOM.nextDouble() < chance) { //0 to 99.9999...
                ItemStack stack = drop.clone();
                stack.setAmount(RANDOM.nextInt(stack.getAmount()) + 1);
                l.getWorld().dropItemNaturally(l, stack);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTab(TabCompleteEvent event) {
        String msg = event.getBuffer();
        if(!msg.startsWith("/")) return; //Not a command. Not our business.
        msg = msg.substring(1);
        String[] args = msg.split(" ", -1);
        String command = args[0];
        CommandRegistry.CommandCategory cat = CommandRegistry.CommandCategory.evaluate(command);
        if(cat == null) return; //Not our command. ?
        LinkedList<String> arguments = prepareArgs(args);
        if(!BaseCommand.hasPermissions(event.getSender(), LibMisc.PERM_USE)) return;

        if(arguments.size() == 2) {
            event.getCompletions().clear();
            String arg = arguments.getLast();
            List<? extends AbstractCmobCommand> commands = CommandRegistry.getAllRegisteredCommands(cat);
            for (AbstractCmobCommand cmd : commands) {
                String cmdStart = cmd.getCommandStart();
                if(cmdStart.toLowerCase().startsWith(arg.toLowerCase()) && BaseCommand.hasPermissions(event.getSender(), BaseCommand.getPermissionNode(cmd))) {
                    event.getCompletions().add(cmdStart);
                }
            }
            return;
        }
        if(cat.equals(CommandRegistry.CommandCategory.CNBT) && arguments.size() == 4) {
            event.getCompletions().clear();
            String suggestedCmob = arguments.get(2);
            CustomMob cmob = CustomMobs.instance.getMobDataHolder().getCustomMob(suggestedCmob);
            if(cmob == null) return;
            String existing = arguments.getLast();
            Collection<String> entries = NBTRegister.getRegister().getEntries((String) cmob.getDataSnapshot().getValue("id"));
            entries.stream().filter(entry -> entry.toLowerCase().startsWith(existing.toLowerCase())).forEach(entry -> event.getCompletions().add(entry));
        }
        if(arguments.size() > 2) {
            String cmdStart = arguments.get(1);
            CommandRegistry.CommandRegisterKey key = new CommandRegistry.CommandRegisterKey(cat, cmdStart);
            AbstractCmobCommand cmd = CommandRegistry.getCommand(key);
            if(cmd == null) return;
            int[] indexes = cmd.getCustomMobArgumentIndex();
            if(indexes.length == 0) return;
            for (int index : indexes) {
                if(index + 1 != arguments.size()) continue;
                event.getCompletions().clear();
                String arg = arguments.getLast().toLowerCase();
                Collection<CustomMob> mobs = CustomMobs.instance.getMobDataHolder().getAllLoadedMobs();
                mobs.stream().filter(mob -> mob.getMobFileName().toLowerCase().startsWith(arg)).forEach(mob -> event.getCompletions().add(mob.getMobFileName()));
            }
        }
    }

    private boolean hasTailingEmptyString(LinkedList<String> list) {
        return list.getLast().isEmpty();
    }

    private LinkedList<String> prepareArgs(String[] args) {
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

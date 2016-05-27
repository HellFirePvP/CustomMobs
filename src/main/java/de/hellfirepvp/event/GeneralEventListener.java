package de.hellfirepvp.event;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.event.CustomMobDeathEvent;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.leash.LeashExecutor;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.nms.NMSReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Random;

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

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Location l = entity.getLocation();

        CustomMob mob = checkEntity(entity);

        if(mob == null) return; //Not our mob.

        CustomMobs.instance.getSpawnLimiter().decrement(mob, entity);
        CustomMob.kill(mob, entity);
        LeashManager.unleash(entity);
        LeashExecutor.cutLeash(entity);

        //Resetting data
        event.getDrops().clear();
        event.setDroppedExp(0);

        CustomMobDeathEvent deathEvent = new CustomMobDeathEvent(mob, entity.getKiller());
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

        CustomMobs.instance.getRespawner().informDeath(mob);
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

package de.hellfirepvp.cmd.ccmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.event.CustomMobSpawnEvent;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.api.exception.SpawnLimitException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCCmobSpawn
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class CommandCCmobSpawn extends AbstractCmobCommand {

    @Override
    public String getCommandStart() {
        return "spawn";
    }

    @Override
    public boolean hasFixedLength() {
        return false;
    }

    @Override
    public int getFixedArgLength() {
        return 0;
    }

    @Override
    public int getMinArgLength() {
        return 6;
    }

    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        String name = args[1];
        String worldName = args[2];
        String xStr = args[3];
        String yStr = args[4];
        String zStr = args[5];
        int amount = 1;
        if(args.length > 6) {
            String argAmount = args[6];
            try {
                amount = Integer.parseInt(argAmount);
            } catch (Exception e) {
                MessageAssist.msgShouldBeAIntNumber(cs, argAmount);
                BaseCommand.sendPlayerDescription(cs, this, false);
                return;
            }
        }

        int x, y, z;

        try {
            x = parseInt(cs, xStr, 0);
        } catch (Exception ignored) {
            return;
        }
        try {
            y = parseInt(cs, yStr, 1);
        } catch (Exception ignored) {
            return;
        }
        try {
            z = parseInt(cs, zStr, 2);
        } catch (Exception ignored) {
            return;
        }

        World w;
        try {
            w = Bukkit.getWorld(worldName);
            w.getName();
        } catch (Exception exc) {
            cs.sendMessage(String.format(LanguageHandler.translate("command.ccmob.spawn.noworld"), worldName));
            BaseCommand.sendPlayerDescription(cs, this, false);
            return;
        }

        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(mob == null) {
            MessageAssist.msgMobDoesntExist(cs, name);
            return;
        }

        int spawned = 0;
        for (int i = 0; i < amount; i++) {
            if(!CustomMobs.instance.getSpawnLimiter().canSpawn(mob.getMobFileName())) {
                cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.limitreached"), mob.getMobFileName()));
                cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.amount"), String.valueOf(spawned), String.valueOf(amount), mob.getMobFileName()));
                return;
            }

            Location spawnAt = new Location(w, x, y, z);
            LivingEntity entity;
            try {
                entity = mob.spawnAt(spawnAt);
            } catch (SpawnLimitException ignored) {
                cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.limitreached"), mob.getMobFileName()));
                cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.amount"), String.valueOf(spawned), String.valueOf(amount), mob.getMobFileName()));
                return;
            }

            CustomMobSpawnEvent event = new CustomMobSpawnEvent(mob.createApiAdapter(), entity, CustomMobSpawnEvent.SpawnReason.COMMAND_CCMOB);
            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) {
                entity.remove();
                cs.sendMessage(LanguageHandler.translate("command.cmob.spawn.cancelled"));
                CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), entity);
                return;
            }
        }
        cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.amount"), String.valueOf(spawned), String.valueOf(amount), mob.getMobFileName()));
    }

    private int parseInt(CommandSender cs, String str, int what) throws Exception {
        try {
            if(cs instanceof BlockCommandSender || cs instanceof Player) {
                if(str.equals("~")) {
                    if(cs instanceof BlockCommandSender) {
                        switch (what) {
                            case 0: return ((BlockCommandSender) cs).getBlock().getX();
                            case 1: return ((BlockCommandSender) cs).getBlock().getY();
                            case 2: return ((BlockCommandSender) cs).getBlock().getZ();
                        }
                    } else {
                        switch (what) {
                            case 0: return ((Player) cs).getLocation().getBlockX();
                            case 1: return ((Player) cs).getLocation().getBlockY();
                            case 2: return ((Player) cs).getLocation().getBlockZ();
                        }
                    }
                } else {
                    return Integer.parseInt(str);
                }
            } else {
                return Integer.parseInt(str);
            }
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(cs, str);
            BaseCommand.sendPlayerDescription(cs, this, false);
            throw new Exception(exc);
        }
        throw new Exception();
    }
}

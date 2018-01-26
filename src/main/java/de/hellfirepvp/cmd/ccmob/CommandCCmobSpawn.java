package de.hellfirepvp.cmd.ccmob;

import org.bukkit.entity.Player;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.LivingEntity;
import de.hellfirepvp.data.mob.CustomMob;
import org.bukkit.World;
import org.bukkit.event.Event;
import de.hellfirepvp.api.data.ICustomMob;
import de.hellfirepvp.api.event.CustomMobSpawnEvent;
import de.hellfirepvp.api.exception.SpawnLimitException;
import org.bukkit.Location;
import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.Bukkit;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.AbstractCmobCommand;

public class CommandCCmobSpawn extends AbstractCmobCommand
{
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
    public void execute(final CommandSender cs, final String[] args) {
        final String name = args[1];
        final String worldName = args[2];
        final String xStr = args[3];
        final String yStr = args[4];
        final String zStr = args[5];
        int amount = 1;
        if (args.length > 6) {
            final String argAmount = args[6];
            try {
                amount = Integer.parseInt(argAmount);
            }
            catch (Exception e) {
                MessageAssist.msgShouldBeAIntNumber(cs, argAmount);
                BaseCommand.sendPlayerDescription(cs, this, false);
                return;
            }
        }
        int x;
        try {
            x = this.parseInt(cs, xStr, 0);
        }
        catch (Exception ignored) {
            return;
        }
        int y;
        try {
            y = this.parseInt(cs, yStr, 1);
        }
        catch (Exception ignored) {
            return;
        }
        int z;
        try {
            z = this.parseInt(cs, zStr, 2);
        }
        catch (Exception ignored) {
            return;
        }
        World w;
        try {
            w = Bukkit.getWorld(worldName);
            w.getName();
        }
        catch (Exception exc) {
            cs.sendMessage(String.format(LanguageHandler.translate("command.ccmob.spawn.noworld"), worldName));
            BaseCommand.sendPlayerDescription(cs, this, false);
            return;
        }
        final CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if (mob == null) {
            MessageAssist.msgMobDoesntExist(cs, name);
            return;
        }
        final int spawned = 0;
        for (int i = 0; i < amount; ++i) {
            if (!CustomMobs.instance.getSpawnLimiter().canSpawn(mob.getMobFileName())) {
                cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.limitreached"), mob.getMobFileName()));
                cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.amount"), String.valueOf(spawned), String.valueOf(amount), mob.getMobFileName()));
                return;
            }
            final Location spawnAt = new Location(w, (double)x, (double)y, (double)z);
            LivingEntity entity;
            try {
                entity = mob.spawnAt(spawnAt);
            }
            catch (SpawnLimitException ignored2) {
                cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.limitreached"), mob.getMobFileName()));
                cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.amount"), String.valueOf(spawned), String.valueOf(amount), mob.getMobFileName()));
                return;
            }
            final CustomMobSpawnEvent event = new CustomMobSpawnEvent(mob.createApiAdapter(), entity, CustomMobSpawnEvent.SpawnReason.COMMAND_CCMOB);
            Bukkit.getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                entity.remove();
                cs.sendMessage(LanguageHandler.translate("command.cmob.spawn.cancelled"));
                CustomMobs.instance.getSpawnLimiter().decrement(mob.getMobFileName(), entity);
                return;
            }
        }
        cs.sendMessage(String.format(LanguageHandler.translate("command.cmob.spawn.amount"), String.valueOf(spawned), String.valueOf(amount), mob.getMobFileName()));
    }
    
    private int parseInt(final CommandSender cs, final String str, final int what) throws Exception {
        try {
            if (!(cs instanceof BlockCommandSender) && !(cs instanceof Player)) {
                return Integer.parseInt(str);
            }
            if (!str.equals("~")) {
                return Integer.parseInt(str);
            }
            if (cs instanceof BlockCommandSender) {
                switch (what) {
                    case 0: {
                        return ((BlockCommandSender)cs).getBlock().getX();
                    }
                    case 1: {
                        return ((BlockCommandSender)cs).getBlock().getY();
                    }
                    case 2: {
                        return ((BlockCommandSender)cs).getBlock().getZ();
                    }
                }
            }
            else {
                switch (what) {
                    case 0: {
                        return ((Player)cs).getLocation().getBlockX();
                    }
                    case 1: {
                        return ((Player)cs).getLocation().getBlockY();
                    }
                    case 2: {
                        return ((Player)cs).getLocation().getBlockZ();
                    }
                }
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(cs, str);
            BaseCommand.sendPlayerDescription(cs, this, false);
            throw new Exception(exc);
        }
        throw new Exception();
    }
}

package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.api.event.CustomMobSpawnEvent;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.lib.LibMisc;
import de.hellfirepvp.spawning.SpawnLimitException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashSet;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 20:57
 * on Project CustomMobs
 * CommandCmobSpawn
 */
public class CommandCmobSpawn extends PlayerCmobCommand {

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
        return 2;
    }

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];

        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(mob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            return;
        }

        Block blockSpawnAt = getTargetBlock(p);
        if(blockSpawnAt == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Can't find solid block in your view to spawn the mob on");
            return;
        }

        Location spawnAt = blockSpawnAt.getLocation().add(0, 1, 0);

        if(args.length > 2) {
            String amtStr = args[2];
            int amount;
            try {
                amount = Integer.parseInt(amtStr);
            } catch (Exception e) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Can't find solid block in your view to spawn the mob on");
                return;
            }

            int spawned = 0;
            for (int i = 0; i < amount; i++) {
                LivingEntity entity;
                try {
                    entity = mob.spawnAt(spawnAt);
                    spawned++;
                } catch (SpawnLimitException ignored) {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Spawnlimit for " + mob.getMobFileName() + " reached!");
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Spawned " + spawned + "/" + amount + " of " + mob.getMobFileName() + ".");
                    return;
                }

                CustomMobSpawnEvent event = new CustomMobSpawnEvent(mob, entity, CustomMobSpawnEvent.SpawnReason.COMMAND_CMOB);
                Bukkit.getPluginManager().callEvent(event);

                if(event.isCancelled()) {
                    entity.remove();
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Spawning was cancelled. Nothing will be spawned.");
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Spawned " + spawned + "/" + amount + " of " + mob.getMobFileName() + ".");
                    return;
                }
            }
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Spawned " + spawned + "/" + amount + " of " + mob.getMobFileName() + ".");
        } else {
            LivingEntity entity;
            try {
                entity = mob.spawnAt(spawnAt);
            } catch (SpawnLimitException ignored) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Spawnlimit for " + mob.getMobFileName() + " reached!");
                return;
            }

            CustomMobSpawnEvent event = new CustomMobSpawnEvent(mob, entity, CustomMobSpawnEvent.SpawnReason.COMMAND_CMOB);
            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) {
                entity.remove();
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Spawning was cancelled. Nothing will be spawned.");
                return;
            }

            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + mob.getMobFileName() + " spawned.");
        }
    }

    private Block getTargetBlock(Player p) {
        try {
            return p.getTargetBlock(LibMisc.TARGET_TRANSPARENT, 80);
        } catch (Throwable tr) {
            return p.getTargetBlock(getNullByteSet(), 80);
        }
    }

    private HashSet<Byte> getNullByteSet() {
        return null;
    }

}

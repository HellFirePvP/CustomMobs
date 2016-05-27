package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.SpawnerDataHolder;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.file.write.SpawnerDataWriter;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.lib.LibMisc;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;

/**
 * HellFirePvP@Admin
 * Date: 15.05.2015 / 22:07
 * on Project CustomMobs
 * CommandCmobSpawner
 */
public class CommandCmobSpawner extends PlayerCmobCommand {

    private static final Random RAND = new Random();

    @Override
    public String getCommandStart() {
        return "spawner";
    }

    @Override
    public boolean hasFixedLength() {
        return false;
    }

    @Override
    public int getFixedArgLength() {
        return 2;
    }

    @Override
    public int getMinArgLength() {
        return 2;
    }

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        int delay;
        if(args.length > 2) {
            String delayStr = args[2];
            try {
                delay = Integer.parseInt(delayStr);
                if(delay < 0) delay = 1;
            } catch (Exception exc) {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + delayStr + " should be a number!");
                BaseCommand.sendPlayerDescription(p, this, true);
                return;
            }
        } else {
            delay = 20 + RAND.nextInt(10);
        }

        Block blockSpawnAt = getTargetBlock(p);
        if(blockSpawnAt == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Can't find solid block in view to set as spawner.");
            return;
        }

        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(mob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            return;
        }

        switch (SpawnerDataWriter.setSpawner(blockSpawnAt.getLocation(), new SpawnerDataHolder.Spawner(mob, delay, args.length > 2))) {
            case LOCATION_OCCUPIED:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "At the location you're looking at is already a spawner.");
                break;
            case IO_EXCEPTION:
                MessageAssist.msgIOException(p);
                break;
            case SUCCESS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "New spawner set! The spawner will spawn " + name +
                        (args.length > 2 ? " in an interval of " + delay + " seconds!" :
                                " randomly in an interval of 20-29 seconds."));
                break;
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

package de.hellfirepvp.cmd.cmob;

import de.hellfirepvp.CustomMobs;
import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.SpawnerDataHolder;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.file.write.SpawnerDataWriter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.lib.LibMisc;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCmobSpawner
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:07
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
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
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
                MessageAssist.msgShouldBeAIntNumber(p, delayStr);
                BaseCommand.sendPlayerDescription(p, this, true);
                return;
            }
        } else {
            delay = 20 + RAND.nextInt(10);
        }

        Block blockSpawnAt = getTargetBlock(p);
        if(blockSpawnAt == null) {
            p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.spawner.noblock"));
            return;
        }

        CustomMob mob = CustomMobs.instance.getMobDataHolder().getCustomMob(name);
        if(mob == null) {
            MessageAssist.msgMobDoesntExist(p, name);
            return;
        }

        switch (SpawnerDataWriter.setSpawner(blockSpawnAt.getLocation(), new SpawnerDataHolder.Spawner(mob.createApiAdapter(), delay, args.length > 2))) {
            case LOCATION_OCCUPIED:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + LanguageHandler.translate("command.cmob.spawner.alreadyset"));
                break;
            case IO_EXCEPTION:
                MessageAssist.msgIOException(p);
                break;
            case SUCCESS:
                if(args.length > 2) {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.spawner.delay"), name, String.valueOf(delay)));
                } else {
                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cmob.spawner.random"), name));
                }
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

package de.hellfirepvp.cmd.cspawn;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import de.hellfirepvp.api.data.callback.SpawnSettingsCallback;
import java.util.Iterator;
import java.util.List;
import de.hellfirepvp.api.data.ISpawnSettings;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import de.hellfirepvp.data.SpawnSettingsHolder;
import java.util.Collection;
import java.util.Collections;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.cmd.BaseCommand;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.MessageAssist;
import org.bukkit.block.Biome;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import de.hellfirepvp.cmd.PlayerCmobCommand;

public class CommandCspawnAdd extends PlayerCmobCommand
{
    @Override
    public void execute(final Player p, final String[] args) {
        final String name = args[1];
        final String boolGrpSpawnStr = args[2];
        final String intGrpSpawnAmt = args[3];
        final String doubleSpawnRate = args[4];
        final List<Biome> biomes = new ArrayList<Biome>();
        final List<String> worlds = new ArrayList<String>();
        final List<String> regions = new ArrayList<String>();
        int grpSpawnAmt;
        try {
            grpSpawnAmt = Integer.parseInt(intGrpSpawnAmt);
            if (grpSpawnAmt < 1) {
                grpSpawnAmt = 1;
            }
            if (grpSpawnAmt > 40) {
                grpSpawnAmt = 40;
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber((CommandSender)p, intGrpSpawnAmt);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        double spawnRate;
        try {
            spawnRate = Double.parseDouble(doubleSpawnRate);
            if (spawnRate < 0.0) {
                spawnRate = 0.0;
            }
            if (spawnRate > 1.0) {
                spawnRate = 1.0;
            }
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeAFloatNumber((CommandSender)p, doubleSpawnRate);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        boolean grpSpawn;
        try {
            grpSpawn = Boolean.parseBoolean(boolGrpSpawnStr);
        }
        catch (Exception exc) {
            MessageAssist.msgShouldBeABooleanValue((CommandSender)p, boolGrpSpawnStr);
            BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
            return;
        }
        if (args.length > 5) {
            for (int additionalParams = Math.min(3, args.length - 5), i = 0; i < additionalParams; ++i) {
                final String in = args[5 + i];
                if (in.length() >= 3) {
                    final char identifier = in.charAt(0);
                    final String inArr = in.substring(2, in.length());
                    final String[] arr = inArr.split(",");
                    switch (identifier) {
                        case 'b': {
                            for (final String s : arr) {
                                try {
                                    final Biome b = Biome.valueOf(s);
                                    if (b == null) {
                                        throw new Exception();
                                    }
                                    biomes.add(b);
                                }
                                catch (Exception e) {
                                    p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.biome"), s));
                                    BaseCommand.sendPlayerDescription((CommandSender)p, this, true);
                                    final Biome[] barr = Biome.values();
                                    final StringBuilder b2 = new StringBuilder();
                                    for (final Biome bio : barr) {
                                        if (b2.length() > 0) {
                                            b2.append(", ");
                                        }
                                        if (bio != null) {
                                            b2.append(bio.name());
                                        }
                                    }
                                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.error.biome.valid"), b2.toString()));
                                    return;
                                }
                            }
                            break;
                        }
                        case 'w': {
                            Collections.addAll(worlds, arr);
                            break;
                        }
                        case 'r': {
                            Collections.addAll(regions, arr);
                            break;
                        }
                    }
                }
            }
        }
        final SpawnSettingsHolder.SpawnSettings settings = new SpawnSettingsHolder.SpawnSettings(grpSpawn, !biomes.isEmpty(), !worlds.isEmpty(), !regions.isEmpty(), grpSpawnAmt, biomes, worlds, regions, spawnRate);
        switch (SpawnSettingsWriter.setSpawnSettings(name, settings)) {
            case MOB_ALREADY_EXISTS: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cspawn.add.exists"), name));
                break;
            }
            case IO_EXCEPTION: {
                MessageAssist.msgIOException((CommandSender)p);
                break;
            }
            case SUCCESS: {
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.success"), name));
                p.sendMessage(ChatColor.GOLD + LanguageHandler.translate("command.cspawn.add.info.properties"));
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.spawnrate"), String.valueOf(spawnRate)));
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.group"), String.valueOf(grpSpawn), String.valueOf(grpSpawnAmt)));
                if (!biomes.isEmpty()) {
                    final StringBuilder builder = new StringBuilder();
                    boolean first = true;
                    for (final Biome b3 : biomes) {
                        if (!first) {
                            builder.append(", ");
                        }
                        builder.append(b3.name());
                        first = false;
                    }
                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.biomes"), builder.toString()));
                }
                if (!worlds.isEmpty()) {
                    final StringBuilder builder = new StringBuilder();
                    boolean first = true;
                    for (final String world : worlds) {
                        if (!first) {
                            builder.append(", ");
                        }
                        builder.append(world);
                        first = false;
                    }
                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.worlds"), builder.toString()));
                }
                if (!regions.isEmpty()) {
                    final StringBuilder builder = new StringBuilder();
                    boolean first = true;
                    for (final String region : regions) {
                        if (!first) {
                            builder.append(", ");
                        }
                        builder.append(region);
                        first = false;
                    }
                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.regions"), builder.toString()));
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public int[] getCustomMobArgumentIndex() {
        return new int[] { 2 };
    }
    
    @Override
    public String getCommandStart() {
        return "add";
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
        return 5;
    }
}

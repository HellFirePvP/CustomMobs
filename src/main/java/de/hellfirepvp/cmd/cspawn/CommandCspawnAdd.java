package de.hellfirepvp.cmd.cspawn;

import de.hellfirepvp.cmd.BaseCommand;
import de.hellfirepvp.cmd.MessageAssist;
import de.hellfirepvp.cmd.PlayerCmobCommand;
import de.hellfirepvp.data.SpawnSettingsHolder;
import de.hellfirepvp.file.write.SpawnSettingsWriter;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibLanguageOutput;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCspawnAdd
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class CommandCspawnAdd extends PlayerCmobCommand {

    @Override
    public int getCustomMobArgumentIndex() {
        return 2;
    }

    @Override
    public void execute(Player p, String[] args) {
        String name = args[1];
        String boolGrpSpawnStr = args[2];
        String intGrpSpawnAmt = args[3];
        String doubleSpawnRate = args[4];

        List<Biome> biomes = new ArrayList<>();
        List<String> worlds = new ArrayList<>();
        List<String> regions = new ArrayList<>();

        boolean grpSpawn;
        int grpSpawnAmt;
        double spawnRate;

        try {
            grpSpawnAmt = Integer.parseInt(intGrpSpawnAmt);
            if(grpSpawnAmt < 1) grpSpawnAmt = 1;
            if(grpSpawnAmt > 40) grpSpawnAmt = 40;
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAIntNumber(p, intGrpSpawnAmt);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        try {
            spawnRate = Double.parseDouble(doubleSpawnRate);
            if(spawnRate < 0.0F) spawnRate = 0.0F;
            if(spawnRate > 1.0F) spawnRate = 1.0F;
        } catch (Exception exc) {
            MessageAssist.msgShouldBeAFloatNumber(p, doubleSpawnRate);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        try {
            grpSpawn = Boolean.parseBoolean(boolGrpSpawnStr);
        } catch (Exception exc) {
            MessageAssist.msgShouldBeABooleanValue(p, boolGrpSpawnStr);
            BaseCommand.sendPlayerDescription(p, this, true);
            return;
        }

        if(args.length > 5) {
            int additionalParams = Math.min(3, args.length - 5);
            for (int i = 0; i < additionalParams; i++) {
                String in = args[5 + i];
                if(in.length() < 3) continue; //2 or bigger

                char identifier = in.charAt(0);
                String inArr = in.substring(2, in.length());
                String[] arr = inArr.split(",");
                switch (identifier) {
                    case 'b':
                        for(String s : arr) {
                            try {
                                Biome b = Biome.valueOf(s);
                                if(b != null) {
                                    biomes.add(b);
                                } else {
                                    throw new Exception(); //Ugh... change at some point..
                                }
                            } catch (Exception e) {
                                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.biome"), s));
                                BaseCommand.sendPlayerDescription(p, this, true);
                                Biome[] barr = Biome.values();
                                StringBuilder b = new StringBuilder();
                                for (Biome bio : barr) {
                                    if(b.length() > 0) {
                                        b.append(", ");
                                    }
                                    if(bio != null) {
                                        b.append(bio.name());
                                    }
                                }
                                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.error.biome.valid"), b.toString()));
                                return;
                            }
                        }
                        break;
                    case 'w':
                        Collections.addAll(worlds, arr);
                        break;
                    case 'r':
                        Collections.addAll(regions, arr);
                        break;
                }
            }
        }

        SpawnSettingsHolder.SpawnSettings settings =
                new SpawnSettingsHolder.SpawnSettings(grpSpawn, !biomes.isEmpty(), !worlds.isEmpty(), !regions.isEmpty(),
                        grpSpawnAmt, biomes, worlds, regions, spawnRate);

        switch (SpawnSettingsWriter.setSpawnSettings(name, settings)) {
            case MOB_ALREADY_EXISTS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.cspawn.add.exists"), name));
                break;
            case IO_EXCEPTION:
                MessageAssist.msgIOException(p);
                break;
            case SUCCESS:
                p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.success"), name));
                p.sendMessage(ChatColor.GOLD + LanguageHandler.translate("command.cspawn.add.info.properties"));
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.spawnrate"), String.valueOf(spawnRate)));
                p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.group"), String.valueOf(grpSpawn), String.valueOf(grpSpawnAmt)));
                if(!biomes.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    boolean first = true;
                    for(Biome b : biomes) {
                        if(!first) {
                            builder.append(", ");
                        }
                        builder.append(b.name());
                        first = false;
                    }
                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.biomes"), builder.toString()));
                }
                if(!worlds.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    boolean first = true;
                    for(String world : worlds) {
                        if(!first) {
                            builder.append(", ");
                        }
                        builder.append(world);
                        first = false;
                    }
                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.worlds"), builder.toString()));
                }
                if(!regions.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    boolean first = true;
                    for(String region : regions) {
                        if(!first) {
                            builder.append(", ");
                        }
                        builder.append(region);
                        first = false;
                    }
                    p.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.cspawn.add.info.regions"), builder.toString()));
                }
                break;
        }

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

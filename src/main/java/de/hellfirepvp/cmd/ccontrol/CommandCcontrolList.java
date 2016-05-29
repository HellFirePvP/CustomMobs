package de.hellfirepvp.cmd.ccontrol;

import de.hellfirepvp.cmd.AbstractCmobCommand;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.lib.LibConfiguration;
import de.hellfirepvp.lib.LibConstantKeys;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import de.hellfirepvp.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Set;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCcontrolList
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class CommandCcontrolList extends AbstractCmobCommand {

    @Override
    public String getCommandStart() {
        return "list";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 3;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        String biomeName = args[1].replace('_', ' ');
        String category = args[2].toUpperCase(); //All Caps due to Enum conventions

        YamlConfiguration fullcontrolBiomes = LibConfiguration.getFullcontrolBiomeConfiguration();

        if(!fullcontrolBiomes.contains(biomeName)) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.biome"), biomeName));
            Set<String> keys = fullcontrolBiomes.getKeys(false);
            keys = StringUtils.replaceAtAll(keys, " ", "_");
            String ret = StringUtils.connectWithSeperator(keys, ", ");
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.error.biome.valid"), ret));
            return;
        }

        WorldSpawner.CreatureType ct = WorldSpawner.CreatureType.getByName(category);

        if(ct == null) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.category"), category));
            String cats = StringUtils.connectWithSeperator(WorldSpawner.CreatureType.getNames(), ", ");
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.error.category.valid"), cats));
            return;
        }

        Set<String> contents = fullcontrolBiomes.getConfigurationSection(biomeName + "." + category).getKeys(false);

        if(contents == null) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.ccontrol.list.missingcategory"), category, biomeName));
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                    String.format(LanguageHandler.translate("command.ccontrol.list.missingcategory.info"), LibConfiguration.getFullControlBiomeFile().getName()));
            return;
        }

        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED +
                String.format(LanguageHandler.translate("command.ccontrol.list.startlisting"), biomeName, category));

        if(contents.isEmpty()) {
            cs.sendMessage(ChatColor.GREEN + LanguageHandler.translate("command.ccontrol.list.nomobs"));
            return;
        }

        for(String key : contents) {
            ConfigurationSection section = fullcontrolBiomes.getConfigurationSection(biomeName + "." + category + "." + key);
            if(section == null) continue;

            int chance = section.getInt(LibConstantKeys.FULLCONTROL_DATA_WEIGHTEDRANDOM_CHANCE);
            int lower = section.getInt(LibConstantKeys.FULLCONTROL_DATA_SPAWNCOUNT_MINIMUM);
            int higher = section.getInt(LibConstantKeys.FULLCONTROL_DATA_SPAWNCOUNT_MAXIMUM);

            cs.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.ccontrol.list.spawninfo"), key,
                    String.valueOf(lower), String.valueOf(higher), String.valueOf(chance)));
        }
    }

}

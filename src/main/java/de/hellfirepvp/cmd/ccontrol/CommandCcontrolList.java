package de.hellfirepvp.cmd.ccontrol;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import java.util.Collection;
import de.hellfirepvp.util.StringUtils;
import de.hellfirepvp.lang.LanguageHandler;
import org.bukkit.ChatColor;
import de.hellfirepvp.lib.LibLanguageOutput;
import de.hellfirepvp.lib.LibConfiguration;
import org.bukkit.command.CommandSender;
import de.hellfirepvp.cmd.AbstractCmobCommand;

public class CommandCcontrolList extends AbstractCmobCommand
{
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
    public void execute(final CommandSender cs, final String[] args) {
        final String biomeName = args[1].replace('_', ' ');
        final String category = args[2].toUpperCase();
        final YamlConfiguration fullcontrolBiomes = LibConfiguration.getFullcontrolBiomeConfiguration();
        if (!fullcontrolBiomes.contains(biomeName)) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.biome"), biomeName));
            Set<String> keys = (Set<String>)fullcontrolBiomes.getKeys(false);
            keys = StringUtils.replaceAtAll(keys, " ", "_");
            final String ret = StringUtils.connectWithSeperator(keys, ", ");
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.error.biome.valid"), ret));
            return;
        }
        final WorldSpawner.CreatureType ct = WorldSpawner.CreatureType.getByName(category);
        if (ct == null) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.error.category"), category));
            final String cats = StringUtils.connectWithSeperator(WorldSpawner.CreatureType.getNames(), ", ");
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + String.format(LanguageHandler.translate("command.error.category.valid"), cats));
            return;
        }
        final Set<String> contents = (Set<String>)fullcontrolBiomes.getConfigurationSection(biomeName + "." + category).getKeys(false);
        if (contents == null) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.ccontrol.list.missingcategory"), category, biomeName));
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.ccontrol.list.missingcategory.info"), LibConfiguration.getFullControlBiomeFile().getName()));
            return;
        }
        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + String.format(LanguageHandler.translate("command.ccontrol.list.startlisting"), biomeName, category));
        if (contents.isEmpty()) {
            cs.sendMessage(ChatColor.GREEN + LanguageHandler.translate("command.ccontrol.list.nomobs"));
            return;
        }
        for (final String key : contents) {
            final ConfigurationSection section = fullcontrolBiomes.getConfigurationSection(biomeName + "." + category + "." + key);
            if (section == null) {
                continue;
            }
            final int chance = section.getInt("weightedRandomChance");
            final int lower = section.getInt("spawnMinimum");
            final int higher = section.getInt("spawnMaximum");
            cs.sendMessage(ChatColor.GREEN + String.format(LanguageHandler.translate("command.ccontrol.list.spawninfo"), key, String.valueOf(lower), String.valueOf(higher), String.valueOf(chance)));
        }
    }
}

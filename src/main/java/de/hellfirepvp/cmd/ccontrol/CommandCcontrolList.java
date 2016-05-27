package de.hellfirepvp.cmd.ccontrol;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CommandCcontrolList
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class CommandCcontrolList /*extends AbstractCmobCommand*/ {

    /*@Override
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
    public String getInputDescription() {
        return "/ccontrol list <BiomeName> <Water_Creature/Ambient/Creature/Monster>";
    }

    @Override
    public String getCommandUsageDescription() {
        return "Lists mobs and their random-spawn attributes.";
    }

    @Override
    public List<String> getAdditionalInformation() {
        return new ArrayList<>();
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        String biomeName = args[1].replace('_', ' ');
        String category = args[2].toUpperCase(); //All Caps due to Enum conventions

        YamlConfiguration fullcontrolBiomes = LibConfiguration.getFullcontrolBiomeConfiguration();

        if(!fullcontrolBiomes.contains(biomeName)) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "No such biome: " + biomeName);
            Set<String> keys = fullcontrolBiomes.getKeys(false);
            keys = StringUtils.replaceAtAll(keys, " ", "_");
            String ret = StringUtils.connectWithSeperator(keys, ", ");
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Available Biomes: " + ret);
            return;
        }

        NMSListTarget.NMSListInjectTarget target = EnumCreatureTypeHelper.getTarget(category);

        if(target == null) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "No such category: " + category);
            String cats = StringUtils.connectWithSeperator(EnumCreatureTypeHelper.strTargets(), ", ");
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.GREEN + "Available Categories: " + cats);
            return;
        }

        category = EnumCreatureTypeHelper.getStringRepresentation(target);

        Set<String> contents = fullcontrolBiomes.getConfigurationSection(biomeName + "." + category).getKeys(false);

        if(contents == null) {
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Category (" + category + ") doesn't exist for Biome '" + biomeName + "'!");
            cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Please delete the '" + LibConfiguration.getFullControlBiomeFile().getName() + "' File in the FullControl folder to " +
                    "solve this issue. Remember: this also deletes your configuration done with /ccontrol so far.");
            return;
        }

        cs.sendMessage(LibLanguageOutput.PREFIX + ChatColor.RED + "Listing data for " + biomeName + "/" + category + ":");

        if(contents.isEmpty()) {
            cs.sendMessage(ChatColor.GREEN + "No mobs spawn.");
            return;
        }

        for(String key : contents) {
            ConfigurationSection section = fullcontrolBiomes.getConfigurationSection(biomeName + "." + category + "." + key);
            if(section == null) continue;

            int chance = section.getInt(LibConstantKeys.FULLCONTROL_DATA_WEIGHTEDRANDOM_CHANCE);
            int lower = section.getInt(LibConstantKeys.FULLCONTROL_DATA_SPAWNCOUNT_MINIMUM);
            int higher = section.getInt(LibConstantKeys.FULLCONTROL_DATA_SPAWNCOUNT_MAXIMUM);

            cs.sendMessage(ChatColor.GREEN + key + " in Group of " + lower + " to " + higher + ". Chance: " + chance);
        }
    }*/

}

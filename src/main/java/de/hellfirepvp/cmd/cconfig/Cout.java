package de.hellfirepvp.cmd.cconfig;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: Cout
 * Created by HellFirePvP
 * Date: (Header change) 27.05.2016 / 4:05
 */
public class Cout /*extends CmobCommand*/ {

    /*@Override
    public void execute(Player p, String[] args) {
        Collection<String> mobs = CustomMobs.getSpawnSettings().getMobNamesWithSettings();
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(String mob : mobs) {
            if(!first) {
                builder.append(", ");
            }
            builder.append(mob);
            first = false;
        }
        String mobsAsString = builder.toString();

        p.sendMessage(LibLanguageOutput.PREFIX + ChatColor.AQUA + "Mobs that spawn randomly:");
        if(mobsAsString.isEmpty()) {
            p.sendMessage(ChatColor.GREEN + "None");
        } else {
            p.sendMessage(ChatColor.GREEN + mobsAsString);
        }

        p.sendMessage("Debug ->");

        p.sendMessage("mobs that spawn at " + p.getLocation().toString() + " biome: " + p.getLocation().getBlock().getBiome().name() + " ->");

        for(String s : CustomMobs.getSpawnSettings().getMobNamesWithSettings()) {
            SpawnSettingsHolder.SpawnSettings settings = CustomMobs.getSpawnSettings().getSettings(s);
            p.sendMessage("Get Settings " + s);

            if(settings == null) {
                p.sendMessage(s + " no settings!");
                continue;
            }

            p.sendMessage(s + "grpAmount: " + settings.groupAmount);
            p.sendMessage(s + "grpSpawn: " + settings.groupSpawn);
            p.sendMessage(s + "spawnRate: " + settings.spawnRate);

            p.sendMessage(s + "biomes " + settings.biomeSpecified);
            if(settings.biomeSpecified) {
                p.sendMessage(s + "biomesList " + settings.biomes.toString());
            }

            p.sendMessage(s + "worlds " + settings.worldsSpecified);
            if(settings.worldsSpecified) {
                p.sendMessage(s + "worldsList " + settings.worlds.toString());
            }

            p.sendMessage(s + "regions " + settings.regionsSpecified);
            if(settings.regionsSpecified) {
                p.sendMessage(s + "regionList " + settings.regions.toString());
            }
        }

        p.sendMessage("DONE");

        p.sendMessage("-> Mobs that may spawn at your location:");

        p.sendMessage(CustomMobs.getRandomWorldSpawner().collectPossibleSpawnableMobs(p.getLocation()).toString());

        p.sendMessage("-> Mobs that can acutally spawn (limited)");

        for(ICustomMob mob : CustomMobs.getRandomWorldSpawner().subtractLimited(CustomMobs.getRandomWorldSpawner().evaluateMobs(CustomMobs.getRandomWorldSpawner().collectPossibleSpawnableMobs(p.getLocation())))) {
            p.sendMessage(mob.getName());
        }

    }

    @Override
    public String getCommandStart() {
        return "debug";
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public int getFixedArgLength() {
        return 1;
    }

    @Override
    public int getMinArgLength() {
        return 0;
    }

    @Override
    public String getInputDescription() {
        return "debug cmd";
    }

    @Override
    public String getCommandUsageDescription() {
        return "debugging";
    }

    @Override
    public List<String> getAdditionalInformation() {
        return new ArrayList<>();
    }*/
}

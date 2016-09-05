package de.hellfirepvp;

import de.hellfirepvp.api.CustomMobsAPI;
import de.hellfirepvp.chat.ChatController;
import de.hellfirepvp.cmd.CommandRegistry;
import de.hellfirepvp.config.ConfigHandler;
import de.hellfirepvp.data.FullControlHandler;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.data.SpawnSettingsHolder;
import de.hellfirepvp.data.SpawnerDataHolder;
import de.hellfirepvp.data.StackingDataHolder;
import de.hellfirepvp.data.drops.DropChatController;
import de.hellfirepvp.data.mob.CustomMob;
import de.hellfirepvp.data.mob.MobDataHolder;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.event.GeneralEventListener;
import de.hellfirepvp.event.ToolEventListener;
import de.hellfirepvp.event.WorldEventListener;
import de.hellfirepvp.integration.IntegrationHandler;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.leash.LeashExecutor;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.spawning.worldSpawning.RandomWorldSpawnExecutor;
import de.hellfirepvp.spawning.Respawner;
import de.hellfirepvp.spawning.SpawnLimit;
import de.hellfirepvp.spawning.SpawnerHandler;
import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import de.hellfirepvp.tool.ToolController;
import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.util.WrappedPrefixLogger;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: CustomMobs
 * Created by HellFirePvP
 * Date: 23.05.2016 / 22:05
 */
public class CustomMobs extends JavaPlugin {

    public static CustomMobs instance;
    public static WrappedPrefixLogger logger;
    public static String pluginYmlVersion;

    public static SupportedVersions currentVersion;

    private File mobFolder, fullControlFolder, langFileFolder;
    private ConfigHandler config = new ConfigHandler();
    private LanguageHandler languageHandler = new LanguageHandler();

    private RandomWorldSpawnExecutor worldSpawnExecutor = new RandomWorldSpawnExecutor();
    private SpawnerHandler spawnerHandler = new SpawnerHandler();
    private Respawner respawner = new Respawner();
    private FullControlHandler fullControlHandler = new FullControlHandler();
    private ToolController toolController = new ToolController();
    private DropChatController dropController = new DropChatController();
    private WorldSpawner worldSpawner = new WorldSpawner();

    private ChatController chatHandler = new ChatController();

    private MobDataHolder mobDataHolder = new MobDataHolder();
    private SpawnerDataHolder spawnerDataHolder = new SpawnerDataHolder();
    private SpawnSettingsHolder spawnSettings = new SpawnSettingsHolder();
    private RespawnDataHolder respawnSettings = new RespawnDataHolder();
    private SpawnLimit spawnLimiter = new SpawnLimit();
    private StackingDataHolder stackingData = new StackingDataHolder();

    @Override
    public void onLoad() {
        instance = this;
        logger = new WrappedPrefixLogger(Bukkit.getLogger(), "CustomMobs");
        logger.info("Starting loading phase...");

        config.loadFromFile();
        languageHandler.loadLanguageFile();
        pluginYmlVersion = getDescription().getVersion();

        currentVersion = SupportedVersions.getCurrentVersion();

        if(currentVersion != null) {
            logger.info("Current server version is supported by CustomMobs!");
        } else {
            logger.info("Could not find supported server version! Is this server version not supported by CustomMobs?");
        }

        logger.info("Loading finished.");
    }

    @Override
    public void onEnable() {
        logger.info("Enabling CustomMobs (" + pluginYmlVersion + ") on server version " + NMSReflector.VERSION);

        if(!NMSReflector.initialize()) {
            logger.severe("Could not create link to Server version " + NMSReflector.VERSION + "!");
            logger.severe("Are you using a supported server version?");
            disable();
            return;
        } else {
            logger.info("Successfully created links to current server version!");
        }

        /*
            --- General loading stuff first ---
         */
        logger.info("Discovering MobTypes...");
        NMSReflector.mobTypeProvider.discoverMobTypes(); //Loads/finds all mobTypes
        logger.info("Found " + NMSReflector.mobTypeProvider.getTypeNames().size() + " MobTypes!");

        IntegrationHandler.loadIntegrations();

        /*
            --- Loading data ---
         */

        mobDataHolder.reloadAllMobs();
        logger.info("Loaded " + mobDataHolder.getAllLoadedMobs().size() + " mobs!");
        NBTRegister.initializeRegistry();

        spawnSettings.resolveSettings();
        spawnerDataHolder.loadData();
        respawnSettings.loadData();
        spawnLimiter.loadData();
        LeashManager.load();
        stackingData.load();

        /*
            --- Loading and starting Live handlers ---
         */

        worldSpawnExecutor.loadData();
        spawnerHandler.start();
        respawner.start();
        LeashExecutor.start();
        fullControlHandler.readAndPushData();
        worldSpawner.start();

        chatHandler.init();

        if(currentVersion != null)
            getServer().getPluginManager().registerEvents(currentVersion.getAmbiguousEventListener(), this);

        getServer().getPluginManager().registerEvents(new GeneralEventListener(), this);
        getServer().getPluginManager().registerEvents(new WorldEventListener(), this);
        getServer().getPluginManager().registerEvents(new ToolEventListener(), this);

        CommandRegistry.initializeCommands();

        /*
            --- Cosmetic Stuff last ---
         */

        CustomMobsAPI.setApiHandler(new DefaultApiHandler());

        createBiomeInfoFile();

        if(config.enablePluginMetrics()) {
            logger.info("Enabling PluginMetrics...");
            loadPluginMetricsInformation();
        } else {
            logger.info("PluginMetrics not enabled.");
        }

        /*
            --- WE ARE DONE ---
         */

        logger.info("Please note that CustomMobs is !!_NOT_!! reloadable. It may lead to various bugs and data inconsistency.");
        logger.info("Startup Completed. Have fun using CustomMobs to your hearts content.");
    }

    private void loadPluginMetricsInformation() {
        try {
            Metrics metrics = new Metrics(this);
            Metrics.Graph createdGraph = metrics.createGraph("Different Mobs created");
            createdGraph.addPlotter(new Metrics.Plotter() {
                @Override
                public int getValue() {
                    return getMobDataHolder().getAllLoadedMobs().size();
                }
            });
            metrics.start();
            logger.info("Sending information to PluginMetrics.");
            logger.info("Thank you for enabling PluginMetrics! (~HellFirePvP)");
        } catch (IOException e) {
            logger.info("Enabling PluginMetrics failed.");
        }
    }

    private void disable() {
        logger.info("CustomMobs encountered an unexpected situation.");
        logger.info("To prevent further errors, CustomMobs will deactivate itself.");
        getPluginLoader().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        logger.info("Cleaning up...");
        int killed = CustomMob.killAllWithLimit();
        logger.info(killed + " alive mobs with SpawnLimit killed.");
        fullControlHandler.restoreMCDefault();
        logger.info("Fullcontrol restored minecraft biome spawnsettings.");
        logger.info("disabled!");
    }

    public LanguageHandler getLanguageHandler() {
        return languageHandler;
    }

    public ConfigHandler getConfigHandler() {
        return config;
    }

    public ToolController getToolController() {
        return toolController;
    }

    public ChatController getChatHandler() {
        return chatHandler;
    }

    public DropChatController getDropController() {
        return dropController;
    }

    public SpawnerHandler getSpawnerHandler() {
        return spawnerHandler;
    }

    public Respawner getRespawner() {
        return respawner;
    }

    public MobDataHolder getMobDataHolder() {
        return mobDataHolder;
    }

    public SpawnLimit getSpawnLimiter() {
        return spawnLimiter;
    }

    public FullControlHandler getFullControlHandler() {
        return fullControlHandler;
    }

    public RandomWorldSpawnExecutor getWorldSpawnExecutor() {
        return worldSpawnExecutor;
    }

    public SpawnSettingsHolder getSpawnSettings() {
        return spawnSettings;
    }

    public RespawnDataHolder getRespawnSettings() {
        return respawnSettings;
    }

    public SpawnerDataHolder getSpawnerDataHolder() {
        return spawnerDataHolder;
    }

    public StackingDataHolder getStackingData() {
        return stackingData;
    }

    private void createBiomeInfoFile() {
        File biomeInfoFile = new File(instance.getDataFolder(), "biomeInfo.txt");
        if(!biomeInfoFile.exists()) {
            try {
                if(!biomeInfoFile.createNewFile()) return;
            } catch (IOException ignored) {}
        } else {
            return;
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(biomeInfoFile));

            writer.write("Biomes the plugin knows (version " + pluginYmlVersion + "):");
            writer.newLine();

            for(Biome b : Biome.values()) {
                if(b == null) continue;
                writer.newLine();
                writer.write(b.name());
            }

        } catch (Exception ignored) {
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public File getMobDataFolder() {
        if(mobFolder == null) {
            mobFolder = new File(instance.getDataFolder(), "Mobs");
        }
        if(!mobFolder.exists()) mobFolder.mkdirs();
        return mobFolder;
    }

    public File getLanguageFileFolder() {
        if(langFileFolder == null) {
            langFileFolder = new File(instance.getDataFolder(), "lang");
        }
        if(!langFileFolder.exists()) langFileFolder.mkdirs();
        return langFileFolder;
    }


    public File getFullControlDataFolder() {
        if(fullControlFolder == null) {
            fullControlFolder = new File(instance.getDataFolder(), "FullControl");
        }
        if(!fullControlFolder.exists()) fullControlFolder.mkdirs();
        return fullControlFolder;
    }

}

package de.hellfirepvp;

import org.bukkit.block.Biome;
import java.io.BufferedWriter;
import java.io.FileWriter;
import de.hellfirepvp.data.mob.CustomMob;
import java.io.IOException;
import de.hellfirepvp.api.CustomMobsAPI;
import de.hellfirepvp.cmd.CommandRegistry;
import de.hellfirepvp.event.ToolEventListener;
import de.hellfirepvp.event.WorldEventListener;
import org.bukkit.event.Listener;
import de.hellfirepvp.event.GeneralEventListener;
import org.bukkit.plugin.Plugin;
import de.hellfirepvp.leash.LeashExecutor;
import de.hellfirepvp.leash.LeashManager;
import de.hellfirepvp.data.nbt.NBTRegister;
import de.hellfirepvp.integration.IntegrationHandler;
import de.hellfirepvp.nms.NMSReflector;
import de.hellfirepvp.util.ServerType;
import org.bukkit.Bukkit;
import de.hellfirepvp.data.StackingDataHolder;
import de.hellfirepvp.spawning.SpawnLimit;
import de.hellfirepvp.data.RespawnDataHolder;
import de.hellfirepvp.data.SpawnSettingsHolder;
import de.hellfirepvp.data.SpawnerDataHolder;
import de.hellfirepvp.data.mob.MobDataHolder;
import de.hellfirepvp.chat.ChatController;
import de.hellfirepvp.spawning.worldSpawning.WorldSpawner;
import de.hellfirepvp.data.drops.DropChatController;
import de.hellfirepvp.tool.ToolController;
import de.hellfirepvp.data.FullControlHandler;
import de.hellfirepvp.spawning.Respawner;
import de.hellfirepvp.spawning.SpawnerHandler;
import de.hellfirepvp.spawning.worldSpawning.RandomWorldSpawnExecutor;
import de.hellfirepvp.lang.LanguageHandler;
import de.hellfirepvp.config.ConfigHandler;
import java.io.File;
import de.hellfirepvp.util.SupportedVersions;
import de.hellfirepvp.util.WrappedPrefixLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomMobs extends JavaPlugin
{
    public static CustomMobs instance;
    public static WrappedPrefixLogger logger;
    public static String pluginYmlVersion;
    public static SupportedVersions currentVersion;
    private File mobFolder;
    private File fullControlFolder;
    private File langFileFolder;
    private ConfigHandler config;
    private LanguageHandler languageHandler;
    private RandomWorldSpawnExecutor worldSpawnExecutor;
    private SpawnerHandler spawnerHandler;
    private Respawner respawner;
    private FullControlHandler fullControlHandler;
    private ToolController toolController;
    private DropChatController dropController;
    private WorldSpawner worldSpawner;
    private ChatController chatHandler;
    private MobDataHolder mobDataHolder;
    private SpawnerDataHolder spawnerDataHolder;
    private SpawnSettingsHolder spawnSettings;
    private RespawnDataHolder respawnSettings;
    private SpawnLimit spawnLimiter;
    private StackingDataHolder stackingData;
    
    public CustomMobs() {
        this.config = new ConfigHandler();
        this.languageHandler = new LanguageHandler();
        this.worldSpawnExecutor = new RandomWorldSpawnExecutor();
        this.spawnerHandler = new SpawnerHandler();
        this.respawner = new Respawner();
        this.fullControlHandler = new FullControlHandler();
        this.toolController = new ToolController();
        this.dropController = new DropChatController();
        this.worldSpawner = new WorldSpawner();
        this.chatHandler = new ChatController();
        this.mobDataHolder = new MobDataHolder();
        this.spawnerDataHolder = new SpawnerDataHolder();
        this.spawnSettings = new SpawnSettingsHolder();
        this.respawnSettings = new RespawnDataHolder();
        this.spawnLimiter = new SpawnLimit();
        this.stackingData = new StackingDataHolder();
    }
    
    @Override
    public void onLoad() {
        CustomMobs.instance = this;
        (CustomMobs.logger = new WrappedPrefixLogger(Bukkit.getLogger(), "CustomMobs")).info("Starting loading phase...");
        ServerType.resolve();
        CustomMobs.logger.info("Assumed ServerType: " + ServerType.getResolvedType().name());
        CustomMobs.logger.info("Server version: " + NMSReflector.VERSION);
        this.config.loadFromFile();
        this.languageHandler.loadLanguageFile();
        CustomMobs.pluginYmlVersion = this.getDescription().getVersion();
        CustomMobs.logger.info("This Version of custom mobs supports the following server versions:");
        CustomMobs.logger.info(SupportedVersions.getSupportedVersions());
        CustomMobs.currentVersion = SupportedVersions.getCurrentVersion();
        if (CustomMobs.currentVersion != null) {
            CustomMobs.logger.info("Current server version is supported by CustomMobs!");
        }
        else {
            CustomMobs.logger.info("Could not find supported server version! Is this server version not supported by CustomMobs?");
        }
        CustomMobs.logger.info("Loading finished.");
    }
    
    @Override
    public void onEnable() {
        if (!NMSReflector.initialize()) {
            CustomMobs.logger.severe("Could not create link to Server version " + NMSReflector.VERSION + "!");
            CustomMobs.logger.severe("Are you using a supported server version?");
            this.disable();
            return;
        }
        CustomMobs.logger.info("Successfully created links to current server version!");
        CustomMobs.logger.info("Enabling CustomMobs (" + CustomMobs.pluginYmlVersion + ") on server version " + NMSReflector.VERSION);
        CustomMobs.logger.info("Discovering MobTypes...");
        NMSReflector.mobTypeProvider.discoverMobTypes();
        CustomMobs.logger.info("Found " + NMSReflector.mobTypeProvider.getTypeNames().size() + " MobTypes!");
        IntegrationHandler.loadIntegrations();
        this.mobDataHolder.reloadAllMobs();
        CustomMobs.logger.info("Loaded " + this.mobDataHolder.getAllLoadedMobs().size() + " mobs!");
        NBTRegister.initializeRegistry();
        this.spawnSettings.resolveSettings();
        this.spawnerDataHolder.loadData();
        this.respawnSettings.loadData();
        this.spawnLimiter.loadData();
        LeashManager.load();
        this.stackingData.load();
        this.worldSpawnExecutor.loadData();
        this.spawnerHandler.start();
        this.respawner.start();
        LeashExecutor.start();
        this.fullControlHandler.readAndPushData();
        this.worldSpawner.start();
        this.chatHandler.init();
        if (CustomMobs.currentVersion != null) {
            this.getServer().getPluginManager().registerEvents(CustomMobs.currentVersion.getAmbiguousEventListener(), (Plugin)this);
        }
        this.getServer().getPluginManager().registerEvents((Listener)new GeneralEventListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new WorldEventListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ToolEventListener(), (Plugin)this);
        CommandRegistry.initializeCommands();
        CustomMobsAPI.setApiHandler(new DefaultApiHandler());
        this.createBiomeInfoFile();
        if (this.config.enablePluginMetrics()) {
            CustomMobs.logger.info("Enabling PluginMetrics...");
            this.loadPluginMetricsInformation();
        }
        else {
            CustomMobs.logger.info("PluginMetrics not enabled.");
        }
        CustomMobs.logger.info("Please note that CustomMobs is !!_NOT_!! reloadable. It may lead to various bugs and data inconsistency.");
        CustomMobs.logger.info("Startup Completed. Have fun using CustomMobs to your hearts content.");
    }
    
    private void loadPluginMetricsInformation() {
        try {
            final Metrics metrics = new Metrics((Plugin)this);
            final Metrics.Graph createdGraph = metrics.createGraph("Different Mobs created");
            createdGraph.addPlotter(new Metrics.Plotter() {
                @Override
                public int getValue() {
                    return CustomMobs.this.getMobDataHolder().getAllLoadedMobs().size();
                }
            });
            metrics.start();
            CustomMobs.logger.info("Sending information to PluginMetrics.");
            CustomMobs.logger.info("Thank you for enabling PluginMetrics! (~HellFirePvP)");
        }
        catch (IOException e) {
            CustomMobs.logger.info("Enabling PluginMetrics failed.");
        }
    }
    
    private void disable() {
        CustomMobs.logger.info("CustomMobs encountered an unexpected situation.");
        CustomMobs.logger.info("To prevent further errors, CustomMobs will deactivate itself.");
        this.getPluginLoader().disablePlugin((Plugin)this);
    }
    
    @Override
    public void onDisable() {
        CustomMobs.logger.info("Cleaning up...");
        final int killed = CustomMob.killAllWithLimit();
        CustomMobs.logger.info(killed + " alive mobs with SpawnLimit killed.");
        this.fullControlHandler.restoreMCDefault();
        CustomMobs.logger.info("Fullcontrol restored minecraft biome spawnsettings.");
        CustomMobs.logger.info("disabled!");
    }
    
    public LanguageHandler getLanguageHandler() {
        return this.languageHandler;
    }
    
    public ConfigHandler getConfigHandler() {
        return this.config;
    }
    
    public ToolController getToolController() {
        return this.toolController;
    }
    
    public ChatController getChatHandler() {
        return this.chatHandler;
    }
    
    public DropChatController getDropController() {
        return this.dropController;
    }
    
    public SpawnerHandler getSpawnerHandler() {
        return this.spawnerHandler;
    }
    
    public Respawner getRespawner() {
        return this.respawner;
    }
    
    public MobDataHolder getMobDataHolder() {
        return this.mobDataHolder;
    }
    
    public SpawnLimit getSpawnLimiter() {
        return this.spawnLimiter;
    }
    
    public FullControlHandler getFullControlHandler() {
        return this.fullControlHandler;
    }
    
    public RandomWorldSpawnExecutor getWorldSpawnExecutor() {
        return this.worldSpawnExecutor;
    }
    
    public SpawnSettingsHolder getSpawnSettings() {
        return this.spawnSettings;
    }
    
    public RespawnDataHolder getRespawnSettings() {
        return this.respawnSettings;
    }
    
    public SpawnerDataHolder getSpawnerDataHolder() {
        return this.spawnerDataHolder;
    }
    
    public StackingDataHolder getStackingData() {
        return this.stackingData;
    }
    
    private void createBiomeInfoFile() {
        final File biomeInfoFile = new File(CustomMobs.instance.getDataFolder(), "biomeInfo.txt");
        if (!biomeInfoFile.exists()) {
            try {
                if (!biomeInfoFile.createNewFile()) {
                    return;
                }
            }
            catch (IOException ex) {}
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(biomeInfoFile));
                writer.write("Biomes the plugin knows (version " + CustomMobs.pluginYmlVersion + "):");
                writer.newLine();
                for (final Biome b : Biome.values()) {
                    if (b != null) {
                        writer.newLine();
                        writer.write(b.name());
                    }
                }
            }
            catch (IOException ex2) {}
            finally {
                if (writer != null) {
                    try {
                        writer.close();
                    }
                    catch (IOException ex3) {}
                }
            }
        }
    }
    
    public File getMobDataFolder() {
        if (this.mobFolder == null) {
            this.mobFolder = new File(CustomMobs.instance.getDataFolder(), "Mobs");
        }
        if (!this.mobFolder.exists()) {
            this.mobFolder.mkdirs();
        }
        return this.mobFolder;
    }
    
    public File getLanguageFileFolder() {
        if (this.langFileFolder == null) {
            this.langFileFolder = new File(CustomMobs.instance.getDataFolder(), "lang");
        }
        if (!this.langFileFolder.exists()) {
            this.langFileFolder.mkdirs();
        }
        return this.langFileFolder;
    }
    
    public File getFullControlDataFolder() {
        if (this.fullControlFolder == null) {
            this.fullControlFolder = new File(CustomMobs.instance.getDataFolder(), "FullControl");
        }
        if (!this.fullControlFolder.exists()) {
            this.fullControlFolder.mkdirs();
        }
        return this.fullControlFolder;
    }
}

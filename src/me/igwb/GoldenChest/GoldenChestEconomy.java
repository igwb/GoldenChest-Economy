package me.igwb.GoldenChest;
import java.io.IOException;
import java.util.logging.Level;

import me.igwb.GoldenChest.ChestInteraction.ChestInteractor;
import me.igwb.GoldenChest.Database.DatabaseConnector;
import me.igwb.GoldenChest.Vault.VaultConnector;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;


public class GoldenChestEconomy extends JavaPlugin {


    private MyEventListener eventListener;
    private MyCommandExecutor commandExecutor;
    private DatabaseConnector dbConnector;
    private ChestRegisterer chestRegisterer;
    private ChestInteractor myChestInteractor;
    private GoldConverter myGoldConverter;
    private TransactionManager myTransactionManager;
    private VaultConnector myVaultConnector;

    private LWC lwc = null;

    private static int CONFIG_VERSION = 0;

    @Override
    public void onEnable() {

        if (getFileConfig().getBoolean("Chests.useLWC")) {
            if (!setupLWC()) {
                this.getLogger().log(Level.WARNING, "LWC not found! - Please disable the useLWC flag in the config or install LWC");
                lwc = null;
            }
        }

        dbConnector = new DatabaseConnector(this);
        chestRegisterer = new ChestRegisterer(this);
        myChestInteractor = new ChestInteractor(this);
        myGoldConverter = new GoldConverter(Float.parseFloat(getFileConfig().getString("Economy-Settings.nuggetValue")));
        myTransactionManager = new TransactionManager(this);

        myVaultConnector = new VaultConnector(this);

        eventListener = new MyEventListener(this);
        commandExecutor = new MyCommandExecutor(this);

        registerEvents();
        registerCommands();

        try {
            Metrics metrics = new Metrics(this);
            if (metrics.start()) {
                logMessage("Now submitting data to http://mcstats.org for " + this.getDescription().getName());
            } else {
                logMessage("No data is beeing submitted to http://mcstats.org");
            }
        } catch (IOException e) {
            logMessage("Could not submit stats to mcstats.org");
        }

    }

    private boolean setupLWC() {
        if (getServer().getPluginManager().getPlugin("LWC") == null) {
            return false;
        }

        org.bukkit.plugin.Plugin lwcp = Bukkit.getPluginManager().getPlugin("LWC");
        lwc = ((LWCPlugin) lwcp).getLWC();
        return true;
    }

    private void registerEvents() {

        getServer().getPluginManager().registerEvents(eventListener, this);
        getServer().getPluginManager().registerEvents(chestRegisterer, this);
    }

    private void registerCommands() {

        getCommand("registerChest").setExecutor(commandExecutor);

        getCommand("balance").setExecutor(commandExecutor);
        getCommand("money").setExecutor(commandExecutor);
        getCommand("bal").setExecutor(commandExecutor);

        getCommand("grant").setExecutor(commandExecutor);
        getCommand("take").setExecutor(commandExecutor);

        getCommand("pay").setExecutor(commandExecutor);

        getCommand("top").setExecutor(commandExecutor);
    }

    public void logMessage(final String message) {

        getLogger().info(message);
    }

    public void logSevere(final String message) {

        this.getLogger().severe(message);
    }

    public DatabaseConnector getDbConnector() {

        return dbConnector;
    }

    public ChestRegisterer getChestRegisterer() {

        return chestRegisterer;
    }

    public ChestInteractor getChestInteractor() {

        return myChestInteractor;
    }

    public GoldConverter getGoldConverter() {

        return myGoldConverter;
    }

    public TransactionManager getTransactionManager() {

        return myTransactionManager;
    }

    public String getDbPath() {

        return this.getDataFolder() + "/Money.db";
    }

    public FileConfiguration getFileConfig() {

        FileConfiguration config = this.getConfig();
        config.options().copyDefaults();
        this.saveDefaultConfig();

        //Check if the config version matches
        if (config.getInt("version") != CONFIG_VERSION) {
            logSevere("The config is not up to date!");
        }

        return config;
    }

    public LWC getLwc() {

        return lwc;
    }

    public VaultConnector getVaultConnector() {

        return myVaultConnector;
    }
}

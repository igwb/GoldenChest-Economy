package me.igwb.GoldenChest;
import me.igwb.GoldenChest.ChestInteraction.ChestInteractor;
import me.igwb.GoldenChest.Database.DatabaseConnector;
import me.igwb.GoldenChest.Vault.VaultConnector;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
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

    private LWC lwc;

    private static int CONFIG_VERSION = 0;
 //   private static Economy econ = null;

    @Override
    public void onEnable() {

        /*if (!setupEconomy()) {
            logSevere("Vault not found! - Disabeling GoldenChest-Economy");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }*/

        if (!setupLWC()) {
            logSevere("LWC not found! - Disabeling GoldenChest-Economy");
            getServer().getPluginManager().disablePlugin(this);
            return;
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
    }

    private boolean setupLWC() {
        if (getServer().getPluginManager().getPlugin("LWC") == null) {
            logSevere("No LWC found!");
            return false;
        }

        org.bukkit.plugin.Plugin lwcp = Bukkit.getPluginManager().getPlugin("LWC");
        lwc = ((LWCPlugin) lwcp).getLWC();
        return true;
    }
/*
    private boolean setupEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            logMessage("no pl");
            return false;
        }
        return true;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            logMessage("no rsp");
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }
*/
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

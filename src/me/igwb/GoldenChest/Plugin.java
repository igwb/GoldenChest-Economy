package me.igwb.GoldenChest;
import me.igwb.GoldenChest.Database.DatabaseConnector;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Plugin extends JavaPlugin {


    private MyEventListener eventListener;
    private MyCommandExecutor commandExecutor;
    private DatabaseConnector dbConnector;
    private ChestRegisterer chestRegisterer;
    private static Economy econ = null;

    @Override
    public void onEnable() {

        //TODO: Fix vault integration

        /*
        if (!setupEconomy()) {
            logSevere("Vault not found! - Disabeling GoldenChest-Economy");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }*/

        dbConnector = new DatabaseConnector(this);
        chestRegisterer = new ChestRegisterer(this);

        eventListener = new MyEventListener(this);
        commandExecutor = new MyCommandExecutor(this);

        registerEvents();
        registerCommands();
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            logSevere("No vault!");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            logSevere("No rsp!");
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void registerEvents() {

        getServer().getPluginManager().registerEvents(eventListener, this);
        getServer().getPluginManager().registerEvents(chestRegisterer, this);
    }

    private void registerCommands() {

        getCommand("registerChest").setExecutor(commandExecutor);
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


    public String getDbPath() {

        logMessage(this.getDataFolder() + "/Money.db");
        return this.getDataFolder() + "/Money.db";
    }

}

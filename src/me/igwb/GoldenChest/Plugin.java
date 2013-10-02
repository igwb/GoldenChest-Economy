package me.igwb.GoldenChest;
import me.igwb.GoldeChest.Database.DatabaseConnector;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Plugin extends JavaPlugin {


    private MyEventListener eventListener;
    private DatabaseConnector dbConnector;
    private static Economy econ = null;

    @Override
    public void onEnable() {

        if (!setupEconomy()) {
            logSevere("Vault not found! - Disabeling GoldenChest-Economy");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        dbConnector = new DatabaseConnector(this);

        eventListener = new MyEventListener(this);
        registerEvents();

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
    }

    public void logMessage(final String message) {

        getLogger().info(message);
    }

    public void logSevere(final String message) {

        this.getLogger().severe(message);
    }

    public String getDbPath() {

        logMessage(this.getDataFolder() + "/Money.db");
        return this.getDataFolder() + "/Money.db";
    }

}

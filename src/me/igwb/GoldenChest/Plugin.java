package me.igwb.GoldenChest;
import org.bukkit.plugin.java.JavaPlugin;


public class Plugin extends JavaPlugin {


    private MyEventListener eventListener;

    @Override
    public void onEnable() {

        eventListener = new MyEventListener(this);
        registerEvents();

    }

    private void registerEvents() {

        getServer().getPluginManager().registerEvents(eventListener, this);
    }

    public void logMessage(final String message) {

        getLogger().info(message);
    }

}

package me.igwb.GoldenChest;
import org.bukkit.plugin.java.JavaPlugin;


public class Plugin extends JavaPlugin{

    
    MyEventListener EL;
    
    @Override
    public void onEnable() {
        
        EL = new MyEventListener(this);
        registerEvents();
        
    }
    
    private void registerEvents() {
        
        getServer().getPluginManager().registerEvents(EL, this);
    }
    
    public void logMessage(String message) {
        
      getLogger().info(message);
    }
    
}

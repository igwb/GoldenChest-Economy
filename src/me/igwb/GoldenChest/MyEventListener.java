package me.igwb.GoldenChest;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyEventListener implements Listener {

    private GoldenChestEconomy parentPlugin;

    public MyEventListener(final GoldenChestEconomy parent) {

        parentPlugin = parent;
    }

    @EventHandler
    public void onInventoryOpenEvent(final InventoryOpenEvent e) {
        /*
        GoldAmount am = null;

        if (e.getInventory().getHolder() instanceof Chest) {


            am = ChestChecker.getChestBalance((Chest) e.getInventory().getHolder());

        } else if (e.getInventory().getHolder() instanceof DoubleChest) {


            am = ChestChecker.getChestBalance((DoubleChest) e.getInventory().getHolder());
        }

        if (am == null) {
            return;
        }
        parentPlugin.logMessage(am.getBlocks() + " Blocks; " + am.getIngots() + " Ingots; " + am.getNuggets() + " Nuggets");*/
    }

    @EventHandler
    public void onPlayerJoinEvent(final PlayerJoinEvent e) {

        parentPlugin.getDbConnector().addPlayer(e.getPlayer().getName());
    }
}

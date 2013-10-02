package me.igwb.GoldenChest;

import me.igwb.GoldenChest.ChestInteraction.ChestChecker;

import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MyEventListener implements Listener {

    private Plugin parentPlugin;

    public MyEventListener(final Plugin parent) {

        parentPlugin = parent;
    }

    @EventHandler
    public void onInventoryOpenEvent(final InventoryOpenEvent e) {

        GoldAmount am = null;

        if (e.getInventory().getHolder() instanceof Chest) {

            am = ChestChecker.getChestBalance((Chest) e.getInventory().getHolder());

        } else if (e.getInventory().getHolder() instanceof DoubleChest) {

            am = ChestChecker.getChestBalance((DoubleChest) e.getInventory().getHolder());
        }

        if (am == null) {
            return;
        }
        parentPlugin.logMessage(am.getBlocks() + " Blocks; " + am.getIngots() + " Ingots; " + am.getNuggets() + " Nuggets");
    }

}

package me.igwb.GoldenChest;

import java.util.ArrayList;

import me.igwb.GoldenChest.Database.DBAddResult;

import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class ChestRegisterer implements Listener {

    private final Plugin parentPlugin;
    private ArrayList<String> playersRegistering = new ArrayList<>();

    public ChestRegisterer(Plugin parent) {

        parentPlugin = parent;
    }

    public void startsRegistering(Player player) {
        if (!playersRegistering.contains(player.getName())) {
            playersRegistering.add(player.getName());
        }
    }


    @EventHandler
    public void onInventoryOpenEvent(final InventoryOpenEvent e) {

        DBAddResult result, secondResult;

        if (playersRegistering.contains(e.getPlayer().getName())) {
            if (e.getInventory().getHolder() instanceof Chest) {


                result = parentPlugin.getDbConnector().addChest(e.getPlayer().getName(), ((Chest) e.getInventory().getHolder()).getLocation());

                switch (result) {
                case success:
                    ((Player) e.getPlayer()).sendMessage("You have registered one chest successfuly.");
                    break;
                case exists:
                    ((Player) e.getPlayer()).sendMessage("The chest you've tried to register was already registered previously.");
                    break;
                case error:
                    ((Player) e.getPlayer()).sendMessage("An unknown error occured while registering the chest, please contact an administrator!");
                    break;
                default:
                    break;
                }
            } else if (e.getInventory().getHolder() instanceof DoubleChest) {

                result = parentPlugin.getDbConnector().addChest(e.getPlayer().getName(), ((Chest) ((DoubleChest) e.getInventory().getHolder()).getLeftSide()).getLocation());
                secondResult = parentPlugin.getDbConnector().addChest(e.getPlayer().getName(), ((Chest) ((DoubleChest) e.getInventory().getHolder()).getRightSide()).getLocation());

                if (result == DBAddResult.success && secondResult == DBAddResult.success) {

                    ((Player) e.getPlayer()).sendMessage("You have registered one doublechest successfuly.");
                } else if (result == DBAddResult.exists | secondResult == DBAddResult.exists) {

                    if (result == DBAddResult.exists && secondResult == DBAddResult.exists) {

                        ((Player) e.getPlayer()).sendMessage("The doublechest was already registered previously.");
                    } else {
                        ((Player) e.getPlayer()).sendMessage("One half of the chest was already registered. Registered the other half successfuly.");
                    }
                } else if (result == DBAddResult.error && secondResult == DBAddResult.error) {

                    ((Player) e.getPlayer()).sendMessage("An unknow error occured: The doublechest was not registered. Please contact an administrator.");
                }

                if (result == DBAddResult.error | secondResult == DBAddResult.error) {

                    ((Player) e.getPlayer()).sendMessage("An unkonw error occured: One half of the doublechest was not registered. Please contact an administrator.");
                }
            }

            playersRegistering.remove(e.getPlayer().getName());
        }
    }
}

package me.igwb.GoldenChest;

import java.util.ArrayList;

import me.igwb.GoldenChest.Database.DBAddResult;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import com.griefcraft.model.Protection;
import com.griefcraft.model.Protection.Type;

public class ChestRegisterer implements Listener {

    private final GoldenChestEconomy parentPlugin;
    private ArrayList<String> playersRegistering = new ArrayList<>();

    public ChestRegisterer(GoldenChestEconomy parent) {

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

        Location loc1, loc2;
        Protection prot;

        if (playersRegistering.contains(e.getPlayer().getName())) {
            if (e.getInventory().getHolder() instanceof Chest) {

                //Check if the player is in a world where he can register chests.
                if (!parentPlugin.getFileConfig().getList("Worlds").contains(e.getPlayer().getLocation().getWorld().getName())) {
                    ((Player) e.getPlayer()).sendMessage("Chests can not be registered in this world!");
                    return;
                }

                //Get the chests location.
                loc1 = ((Chest) e.getInventory().getHolder()).getLocation();

                //Check the chest with LWC if enabled
                if (parentPlugin.getLwc() != null) {
                    prot = parentPlugin.getLwc().findProtection(loc1.getBlock());

                    if (prot != null) {
                        if (prot.getOwner().equals(e.getPlayer().getName())) {
                            if (prot.getType() != Type.PRIVATE) {
                                ((Player) e.getPlayer()).sendMessage("Warning! The chest you're trying to register is not private!");
                                return;
                            }
                        } else {
                            ((Player) e.getPlayer()).sendMessage("You do not own this chest!");
                            return;
                        }
                    } else {
                        ((Player) e.getPlayer()).sendMessage("You can not register an unprotected chest!");
                        return;
                    }
                }

                //Register the chest
                result = parentPlugin.getDbConnector().addChest(e.getPlayer().getName(), loc1);

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

                //Check if the player is in a world where he can register chests.
                if (!parentPlugin.getFileConfig().getList("Worlds").contains(e.getPlayer().getLocation().getWorld().getName())) {
                    ((Player) e.getPlayer()).sendMessage("Chests can not be registered in this world!");
                    return;
                }

                //Get the chest locations
                loc1 = ((Chest) ((DoubleChest) e.getInventory().getHolder()).getLeftSide()).getLocation();
                loc2 = ((Chest) ((DoubleChest) e.getInventory().getHolder()).getRightSide()).getLocation();

                //Check with LWC if enabled
                if (parentPlugin.getLwc() != null) {
                    //Check protection 1 with LWC
                    prot = parentPlugin.getLwc().findProtection(loc1.getBlock());

                    if (prot != null) {
                        if (prot.getOwner().equals(e.getPlayer().getName())) {
                            if (prot.getType() != Type.PRIVATE) {
                                ((Player) e.getPlayer()).sendMessage("Warning! The chest you're trying to register is not private!");
                                return;
                            }
                        } else {
                            ((Player) e.getPlayer()).sendMessage("You do not own this chest!");
                            return;
                        }
                    } else {
                        ((Player) e.getPlayer()).sendMessage("You can not register an unprotected chest!");
                        return;
                    }

                    //Check protection 2 with LWC
                    prot = parentPlugin.getLwc().findProtection(loc2.getBlock());

                    if (prot != null) {
                        if (prot.getOwner().equals(e.getPlayer().getName())) {
                            if (prot.getType() != Type.PRIVATE) {
                                ((Player) e.getPlayer()).sendMessage("Warning! The chest you're trying to register is not private!");
                                return;
                            }
                        } else {
                            ((Player) e.getPlayer()).sendMessage("You do not own this chest!");
                            return;
                        }
                    } else {
                        ((Player) e.getPlayer()).sendMessage("You can not register an unprotected chest!");
                        return;
                    }
                }

                //Try to register the first chest
                result = parentPlugin.getDbConnector().addChest(e.getPlayer().getName(), loc1);

                //Check if the player is allowed to register another chest;
                if (parentPlugin.getDbConnector().getPlayersChests(e.getPlayer().getName()).size() < parentPlugin.getFileConfig().getInt("Chests.maximumAllowed")) {
                    secondResult = parentPlugin.getDbConnector().addChest(e.getPlayer().getName(), loc2);
                } else {
                    if (result == DBAddResult.success) {
                        ((Player) e.getPlayer()).sendMessage("One chest registered. You're not allowed to have more.");
                    } else {
                        ((Player) e.getPlayer()).sendMessage("No chest was registered. Please try a single chest instead of a double chest.");
                    }
                    return;
                }
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

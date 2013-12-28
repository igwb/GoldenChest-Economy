package me.igwb.GoldenChest;

import java.util.ArrayList;

import me.igwb.GoldenChest.ChestInteraction.ChestChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

public class TransactionManager {

    private Plugin parentPlugin;

    public TransactionManager(Plugin parent) {

        parentPlugin = parent;
    }

    public TransactionResult takeMoney(String player, float amount) {

        ArrayList<Location> chests = parentPlugin.getDbConnector().getPlayersChests(player);
        if (getBalance(player) >= amount) {


            return TransactionResult.successful;
        } else {
            return TransactionResult.insufficientFunds;
        }
    }

    public void giveMoney(String player, float amount) {
        ArrayList<Location> chests = parentPlugin.getDbConnector().getPlayersChests(player);

        //Load the players overflow amount and try to deposit it...
        amount += parentPlugin.getDbConnector().getOveflowAmount(player);

        for (Location loc : chests) {
            amount = parentPlugin.getChestInteractor().depositGold(amount, ((Chest) loc.getBlock().getState())).getNotProcessed();
            if (amount == 0) {
                return;
            }
        }

        //Store the new overflow amount...
        parentPlugin.getDbConnector().setOverflowAmount(player, amount);

    }
    public float getBalance(String player) {

        float balance = 0;
        ArrayList<Location> chests = parentPlugin.getDbConnector().getPlayersChests(player);

        for (Location loc : chests) {
            if (loc.getBlock().getType() == Material.CHEST) {

                balance += parentPlugin.getGoldConverter().convertGoldToMoney(ChestChecker.getChestBalance(((Chest) loc.getBlock().getState())));
            }
        }

        balance += parentPlugin.getDbConnector().getOveflowAmount(player);

        return balance;
    }
}

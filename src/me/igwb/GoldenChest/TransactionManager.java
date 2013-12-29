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

        float amountToTake;
        ArrayList<Location> chests = parentPlugin.getDbConnector().getPlayersChests(player);


        //Check if the overflow amount is sufficient for this transaction
        if (parentPlugin.getDbConnector().getOveflowAmount(player) >= amount) {
            parentPlugin.getDbConnector().setOverflowAmount(player, parentPlugin.getDbConnector().getOveflowAmount(player) - amount);



            return TransactionResult.successful;
        } else {
            amountToTake = amount - parentPlugin.getDbConnector().getOveflowAmount(player);
        }

        if (getBalance(player) >= amount) {

            for (Location loc : chests) {
                amountToTake = parentPlugin.getChestInteractor().takeGold(amountToTake, ((Chest) loc.getBlock().getState())).getNotProcessed();
                if (amountToTake == 0) {
                    parentPlugin.getDbConnector().setOverflowAmount(player, amountToTake);
                    return TransactionResult.successful;
                }
            }

            parentPlugin.getDbConnector().setOverflowAmount(player, amountToTake);

            return TransactionResult.successful;
        } else {
            return TransactionResult.insufficientFunds;
        }
    }

    /**
     * Tries to store the players overflow amount as item in his chests.
     * @param player
     * @return Returns fault if there is not enough space in the chests.
     */
    public Boolean storeOverflowToChest(String player) {

        float overflow = parentPlugin.getDbConnector().getOveflowAmount(player);

        if (overflow >= Float.parseFloat(parentPlugin.getFileConfig().getString("Economy-Settings.nuggetValue"))) {
            takeMoney(player, overflow);
            giveMoney(player, overflow);
        }

        return !(parentPlugin.getDbConnector().getOveflowAmount(player) >= Float.parseFloat(parentPlugin.getFileConfig().getString("Economy-Settings.nuggetValue")));
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

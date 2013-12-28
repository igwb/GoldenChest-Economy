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

    public void takeMoney(float amount, String player) {

        parentPlugin.getDbConnector().getPlayersChests(player);
    }

    public float getBalance(String player) {

        float balance = 0;
        ArrayList<Location> chests = parentPlugin.getDbConnector().getPlayersChests(player);

        for (Location loc : chests) {
            if (loc.getBlock().getType() == Material.CHEST) {

                balance += parentPlugin.getGoldConverter().convertGoldToMoney(ChestChecker.getChestBalance(((Chest) loc.getBlock().getState())));
            }
        }

        return balance;
    }
}

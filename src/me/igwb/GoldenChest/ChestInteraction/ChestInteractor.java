package me.igwb.GoldenChest.ChestInteraction;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import me.igwb.GoldenChest.GoldAmount;
import me.igwb.GoldenChest.GoldTransactionResult;
import me.igwb.GoldenChest.GoldTransactionResultType;
import me.igwb.GoldenChest.GoldenChestEconomy;

public final class ChestInteractor {

    private GoldenChestEconomy parentPlugin;
    public ChestInteractor(GoldenChestEconomy parent) {

        parentPlugin = parent;
    }


    /**
     * @param amount The amount to deposit into the chest
     * @param targetChest Deposit into this chest
     * @return Returns the amount of money that could not be deposited.
     */
    public GoldTransactionResult depositGold(final Float amount, final Chest targetChest) {

        Float chestBalance, targetBalance, amountNotDeposited;

        chestBalance = parentPlugin.getGoldConverter().convertGoldToMoney(ChestChecker.getChestBalance(targetChest));
        targetBalance = chestBalance + amount;

        if (parentPlugin.getFileConfig().getBoolean("Debug")) {
            parentPlugin.logMessage("adding " + amount + " to " + chestBalance + " target: " + targetBalance);
        }

        amountNotDeposited = setGold(targetBalance, targetChest).getNotProcessed();

        if (parentPlugin.getFileConfig().getBoolean("Debug")) {
            parentPlugin.logMessage("not deposited: " + amountNotDeposited);
        }

        return new GoldTransactionResult(GoldTransactionResultType.SUCCESSFUL, amountNotDeposited);
    }

    /**
     * @param amount The amount to set in the target chest
     * @param targetChest Modify this chest
     * @return Returns the amount of money that could not be deposited.
     */
    public GoldTransactionResult setGold(float amount, Chest targetChest) {

        GoldAmount goldToDeposit;
        Integer blocks = 0, ingots = 0, nuggets = 0;
        ItemStack stack = null;

        goldToDeposit = parentPlugin.getGoldConverter().convertMoneyToGold(amount);
        blocks = goldToDeposit.getBlocks();
        ingots = goldToDeposit.getIngots();
        nuggets = goldToDeposit.getNuggets();


        ItemStack[] contents = targetChest.getBlockInventory().getContents();


        //Remove all currency items previously present.
        for (int i = 0; i < targetChest.getBlockInventory().getSize(); i++) {
            stack = contents[i];
            if (stack != null) {
                switch (stack.getType()) {
                case GOLD_BLOCK:
                    stack.setAmount(0);
                    break;
                case GOLD_INGOT:
                    stack.setAmount(0);
                    break;
                case GOLD_NUGGET:
                    stack.setAmount(0);
                    break;
                default:
                    break;
                }

                targetChest.getBlockInventory().setItem(i, stack);
            }
        }

        //Deposit the new items.
        int empty;
        stack = null;

        while (blocks > 0 | nuggets > 0 | ingots > 0) {
            empty = targetChest.getBlockInventory().firstEmpty();

            if (empty < 0) {
                break;
            }

            if (blocks >= 1) {
                stack = new ItemStack(Material.GOLD_BLOCK, 1);
                if (blocks <= stack.getMaxStackSize()) {
                    stack.setAmount(blocks);
                    blocks = 0;
                } else {
                    stack.setAmount(stack.getMaxStackSize());
                    blocks -= stack.getMaxStackSize();
                }
            } else if (ingots >= 1) {
                stack = new ItemStack(Material.GOLD_INGOT, 1);
                if (ingots <= stack.getMaxStackSize()) {
                    stack.setAmount(ingots);
                    ingots = 0;
                } else {
                    stack.setAmount(stack.getMaxStackSize());
                    ingots -= stack.getMaxStackSize();
                }
            } else if (nuggets >= 1) {
                stack = new ItemStack(Material.GOLD_NUGGET, 1);
                if (nuggets <= stack.getMaxStackSize()) {
                    stack.setAmount(nuggets);
                    nuggets = 0;
                } else {
                    stack.setAmount(stack.getMaxStackSize());
                    nuggets -= stack.getMaxStackSize();
                }
            }
            targetChest.getBlockInventory().setItem(empty, stack);
        }

        if (blocks == 0 && ingots == 0 && nuggets == 0) {
            return new GoldTransactionResult(GoldTransactionResultType.SUCCESSFUL, goldToDeposit.getoverflowMoney());
        } else {
            if (goldToDeposit.getBlocks() == blocks && goldToDeposit.getIngots() == ingots && goldToDeposit.getNuggets() == nuggets) {
                return new GoldTransactionResult(GoldTransactionResultType.FAILED, amount);
            } else {
                return new GoldTransactionResult(GoldTransactionResultType.SUCCESSFUL, parentPlugin.getGoldConverter().convertGoldToMoney(new GoldAmount(blocks, ingots, nuggets, goldToDeposit.getoverflowMoney())));
            }
        }

    }

    /**
     * @param amount The amount to take from the target chest
     * @param targetChest Take from this chest
     * @return Returns the amount of money that could not be deposited.
     */
    public GoldTransactionResult takeGold(float amount, Chest targetChest) {

        Float chestBalance, targetBalance, amountNotTaken;

        chestBalance = parentPlugin.getGoldConverter().convertGoldToMoney(ChestChecker.getChestBalance(targetChest));

        if (chestBalance < amount) {

            targetBalance = 0f;
            amountNotTaken = amount - chestBalance;
        } else {

            targetBalance = chestBalance - amount;
            amountNotTaken = 0f;
        }

        if (parentPlugin.getFileConfig().getBoolean("Debug")) {
            parentPlugin.logMessage("taking " + amount + " from " + chestBalance + " target: " + targetBalance);
        }

        amountNotTaken += setGold(targetBalance, targetChest).getNotProcessed();

        if (parentPlugin.getFileConfig().getBoolean("Debug")) {
            parentPlugin.logMessage("not deposited: " + amountNotTaken);
        }

        return new GoldTransactionResult(GoldTransactionResultType.SUCCESSFUL, amountNotTaken);
    }

}

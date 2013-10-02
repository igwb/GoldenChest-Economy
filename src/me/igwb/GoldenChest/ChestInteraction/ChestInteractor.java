package me.igwb.GoldenChest.ChestInteraction;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import me.igwb.GoldenChest.GoldTransactionResult;
import me.igwb.GoldenChest.GoldTransactionResultType;

public class ChestInteractor {


    public GoldTransactionResult depositGold(final Float amount, final Chest targetChest) {

        Integer blocks = 0, ingots = 0;
        Float amountToProcess = amount;

        //Give as many gold blocks as possible.
        if (amountToProcess / 9 >= 1) {
            blocks = (int) Math.floor(amount / 9);
            amountToProcess -= blocks * 9;
        }

        //Give as many gold ingots as possible.
        if (amountToProcess / 1 >= 1) {
            ingots = (int) Math.floor(amountToProcess / 1);
            amountToProcess -= ingots * 1;
        }

        ItemStack[] contents = targetChest.getInventory().getContents();

        //Try to deposit the items on existing stacks.
        for (ItemStack itemStack : contents) {
            if (itemStack != null) {
                switch (itemStack.getType()) {
                case GOLD_BLOCK:
                    if (blocks >= 1) {
                        if (itemStack.getMaxStackSize() - itemStack.getAmount() >= blocks) {
                            itemStack.setAmount(itemStack.getAmount() + blocks);
                            blocks = 0;
                        } else {
                            blocks -= itemStack.getMaxStackSize() - itemStack.getAmount();
                            itemStack.setAmount(itemStack.getMaxStackSize());
                        }
                    }
                    break;
                case GOLD_INGOT:
                    if (blocks >= 1) {
                        if (itemStack.getMaxStackSize() - itemStack.getAmount() >= ingots) {
                            itemStack.setAmount(itemStack.getAmount() + ingots);
                            ingots = 0;
                        } else {
                            ingots -= itemStack.getMaxStackSize() - itemStack.getAmount();
                            itemStack.setAmount(itemStack.getMaxStackSize());
                        }
                    }
                    break;
                default:
                    break;
                }
            }
        }

        //Create new item stacks if not all items are deposited already.
        if (blocks > 0 | ingots > 0) {
            for (ItemStack itemStack : contents) {
                if (itemStack == null) {
                    if (blocks > 0) {
                        itemStack = new ItemStack(Material.GOLD_BLOCK);
                        if (blocks < itemStack.getMaxStackSize()) {
                            itemStack.setAmount(blocks);
                            blocks = 0;
                        } else {
                            itemStack.setAmount(itemStack.getMaxStackSize());
                            blocks -= itemStack.getMaxStackSize();
                        }
                    } else if (ingots > 0) {
                        itemStack = new ItemStack(Material.GOLD_INGOT);
                        if (ingots < itemStack.getMaxStackSize()) {
                            itemStack.setAmount(ingots);
                            ingots = 0;
                        } else {
                            itemStack.setAmount(itemStack.getMaxStackSize());
                            ingots -= itemStack.getMaxStackSize();
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        if (blocks == 0 && ingots == 0) {
            return new GoldTransactionResult(GoldTransactionResultType.SUCCESSFUL, amountToProcess);
        } else {
            amountToProcess += (blocks * 9) + ingots;
           if (amountToProcess == amount) {
               return new GoldTransactionResult(GoldTransactionResultType.FAILED, amount);
           } else {
               return new GoldTransactionResult(GoldTransactionResultType.SUCCESSFUL, amountToProcess);
           }
        }
    }

}

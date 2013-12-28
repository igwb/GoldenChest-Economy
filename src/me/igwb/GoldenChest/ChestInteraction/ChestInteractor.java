package me.igwb.GoldenChest.ChestInteraction;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import me.igwb.GoldenChest.GoldAmount;
import me.igwb.GoldenChest.GoldTransactionResult;
import me.igwb.GoldenChest.GoldTransactionResultType;
import me.igwb.GoldenChest.Plugin;

public final class ChestInteractor {

    private Plugin parentPlugin;
    public ChestInteractor(Plugin parent) {

        parentPlugin = parent;
    }


    /**
     * @param amount The amount to deposit
     * @param targetChest Deposit into this chest
     * @return Returns the amount of money that could not be deposited.
     */
    public GoldTransactionResult depositGold(final Float amount, final Chest targetChest) {

        GoldAmount goldToDeposit;
        Integer blocks = 0, ingots = 0, nuggets = 0;

        goldToDeposit = parentPlugin.getGoldConverter().convertMoneyToGold(amount);


        blocks = goldToDeposit.getBlocks();
        ingots = goldToDeposit.getIngots();
        nuggets = goldToDeposit.getNuggets();

        ItemStack[] contents = targetChest.getBlockInventory().getContents();

        //Try to deposit the items on existing stacks
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
                    if (ingots >= 1) {
                        if (itemStack.getMaxStackSize() - itemStack.getAmount() >= ingots) {
                            itemStack.setAmount(itemStack.getAmount() + ingots);
                            ingots = 0;
                        } else {
                            ingots -= itemStack.getMaxStackSize() - itemStack.getAmount();
                            itemStack.setAmount(itemStack.getMaxStackSize());
                        }
                    }
                    break;
                case GOLD_NUGGET:
                    if (nuggets >= 1) {
                        if (itemStack.getMaxStackSize() - itemStack.getAmount() >= nuggets) {
                            itemStack.setAmount(itemStack.getAmount() + nuggets);
                            nuggets = 0;
                        } else {
                            nuggets -= itemStack.getMaxStackSize() - itemStack.getAmount();
                            itemStack.setAmount(itemStack.getMaxStackSize());
                        }
                    }
                default:
                    break;
                }
            }
        }


        //Create new item stacks if not all items are deposited already.

        int empty;
        ItemStack stack = null;

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

}

package me.igwb.GoldenChest.ChestInteraction;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import me.igwb.GoldenChest.GoldAmount;
import me.igwb.GoldenChest.GoldConverter;
import me.igwb.GoldenChest.GoldTransactionResult;
import me.igwb.GoldenChest.GoldTransactionResultType;

public class ChestInteractor {


    public GoldTransactionResult depositGold(final Float amount, final Chest targetChest) {

        GoldAmount goldToDeposit;
        Integer blocks = 0, ingots = 0, nuggets = 0;

        GoldConverter gc = new GoldConverter((float) 1.0);
        goldToDeposit = gc.convertMoneyToGold(amount);


        blocks = goldToDeposit.getBlocks();
        ingots = goldToDeposit.getIngots();
        nuggets = goldToDeposit.getNuggets();

        ItemStack[] contents = targetChest.getInventory().getContents();

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
        if (blocks > 0 | ingots > 0 | nuggets > 0) {
            for (ItemStack itemStack : contents) {
                if (itemStack == null) {
                    if (blocks >= 1) {
                        itemStack = new ItemStack(Material.GOLD_BLOCK);
                        if (blocks < itemStack.getMaxStackSize()) {
                            itemStack.setAmount(blocks);
                            blocks = 0;
                        } else {
                            itemStack.setAmount(itemStack.getMaxStackSize());
                            blocks -= itemStack.getMaxStackSize();
                        }
                    } else if (ingots >= 1) {
                        itemStack = new ItemStack(Material.GOLD_INGOT);
                        if (ingots < itemStack.getMaxStackSize()) {
                            itemStack.setAmount(ingots);
                            ingots = 0;
                        } else {
                            itemStack.setAmount(itemStack.getMaxStackSize());
                            ingots -= itemStack.getMaxStackSize();
                        }
                    } else if (nuggets >= 1) {
                        itemStack = new ItemStack(Material.GOLD_NUGGET);
                        if (nuggets < itemStack.getMaxStackSize()) {
                            itemStack.setAmount(nuggets);
                            nuggets = 0;
                        } else {
                            itemStack.setAmount(itemStack.getMaxStackSize());
                            nuggets -= itemStack.getMaxStackSize();
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        if (blocks == 0 && ingots == 0 && nuggets == 0) {
            return new GoldTransactionResult(GoldTransactionResultType.SUCCESSFUL, goldToDeposit.getoverflowMoney());
        } else {
            if (goldToDeposit.getBlocks() == blocks && goldToDeposit.getIngots() == ingots && goldToDeposit.getNuggets() == nuggets) {
                return new GoldTransactionResult(GoldTransactionResultType.FAILED, amount);
            } else {
                return new GoldTransactionResult(GoldTransactionResultType.SUCCESSFUL, gc.convertGoldToMoney(new GoldAmount(blocks, ingots, nuggets, goldToDeposit.getoverflowMoney())));
            }
        }
    }

}

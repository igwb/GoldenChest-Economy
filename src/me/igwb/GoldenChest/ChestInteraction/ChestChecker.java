package me.igwb.GoldenChest.ChestInteraction;

import me.igwb.GoldenChest.GoldAmount;

import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public final class ChestChecker {

    private ChestChecker() {

    }

    public static GoldAmount getChestBalance(final Chest checkThis) {

        ItemStack[] contents = checkThis.getBlockInventory().getContents();

        int blocks = 0, ingots = 0, nuggets = 0;

        for (ItemStack is : contents) {

            if (is != null) {
                switch (is.getType()) {
                case GOLD_BLOCK:

                    blocks += is.getAmount();
                    break;
                case GOLD_INGOT:

                    ingots += is.getAmount();
                    break;
                case GOLD_NUGGET:

                    nuggets += is.getAmount();
                    break;
                default:
                    break;
                }
            }
        }

        return new GoldAmount(blocks, ingots, nuggets);
    }

    /*public static GoldAmount getChestBalance(final DoubleChest checkThis) {

        ItemStack[] contents = checkThis.getInventory().getContents();

        int blocks = 0, ingots = 0, nuggets = 0;

        for (ItemStack is : contents) {

            if (is != null) {
                switch (is.getType()) {
                case GOLD_BLOCK:

                    blocks += is.getAmount();
                    break;
                case GOLD_INGOT:

                    ingots += is.getAmount();
                    break;
                case GOLD_NUGGET:

                    nuggets += is.getAmount();
                    break;
                default:
                    break;
                }
            }
        }

        return new GoldAmount(blocks, ingots, nuggets);
    }*/

}

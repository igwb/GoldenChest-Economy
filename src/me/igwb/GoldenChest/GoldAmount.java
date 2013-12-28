package me.igwb.GoldenChest;

public class GoldAmount {

    private final int blockCount, ingotCount, nuggetCount;
    private final float overflowAmount;


    public GoldAmount(final int blocks, final int ingots, final int nuggets) {

        blockCount = blocks;
        ingotCount = ingots;
        nuggetCount = nuggets;
        overflowAmount = 0;
    }

    public GoldAmount(final int blocks, final int ingots, final int nuggets, final float overflowMoney) {

        blockCount = blocks;
        ingotCount = ingots;
        nuggetCount = nuggets;
        overflowAmount = overflowMoney;
    }

    public int getBlocks() {

        return blockCount;
    }

    public int getIngots() {

        return ingotCount;
    }

    public int getNuggets() {

        return nuggetCount;
    }

    public float getoverflowMoney() {

        return overflowAmount;
    }

    public String toString() {

        return "Blocks " + blockCount + " Ingots " + ingotCount + " Nuggets " + nuggetCount + " Overflow " + overflowAmount;
    }

}

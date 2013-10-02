package me.igwb.GoldenChest;

public class GoldAmount {

    private final int blockCount, ingotCount, nuggetCount;

    public GoldAmount(final int blocks, final int ingots, final int nuggets) {

        blockCount = blocks;
        ingotCount = ingots;
        nuggetCount = nuggets;
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

}

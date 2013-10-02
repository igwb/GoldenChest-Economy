package me.igwb.GoldenChest;

public class GoldAmount {

    private int blocks, ingots, nuggets;
    
    public GoldAmount(int blocks, int ingots, int nuggets) {
        
        this.blocks = blocks;
        this.ingots = ingots;
        this.nuggets = nuggets;
    }
    
    public int getBlocks() {
        
        return blocks;
    }
    
    public int getIngots() {
        
        return ingots;
    }
    
    public int getNuggets() {
        
        return nuggets;
    }
    
}

package me.igwb.GoldenChest;

public class GoldConverter {

    private final float exchangeRate;

    public GoldConverter(final float nuggetValue) {

        if (nuggetValue > 0) {
            exchangeRate = nuggetValue;
        } else {
            throw new IllegalArgumentException("NuggetValue must be bigger than 0!");
        }
    }

    public float convertGoldToMoney(GoldAmount toConvert) {

        float value = 0;

        value += toConvert.getNuggets() * exchangeRate;
        value += toConvert.getIngots() * 9 * exchangeRate;
        value += toConvert.getBlocks() * 9 * 9 * exchangeRate;
        value += toConvert.getoverflowMoney();

        return value;
    }

    public GoldAmount convertMoneyToGold(final Float amount) {

        int blocks, ingots, nuggets;
        float overflowMoney, toConvert;

        toConvert = amount;

        blocks = (int) Math.floor(toConvert / (9 * 9 * exchangeRate));
        toConvert -= 9 * 9 * exchangeRate * blocks;

        ingots = (int) Math.floor(toConvert / (9 * exchangeRate));
        toConvert -= 9 * exchangeRate * ingots;

        nuggets = (int) Math.floor(toConvert / exchangeRate);
        toConvert -= nuggets * exchangeRate;

        overflowMoney = toConvert;

        return new GoldAmount(blocks, ingots, nuggets, overflowMoney);
    }

}

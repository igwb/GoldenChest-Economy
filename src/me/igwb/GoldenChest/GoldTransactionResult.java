package me.igwb.GoldenChest;

public class GoldTransactionResult {

    private final GoldTransactionResultType result;
    private final Float notProcessed;

    public GoldTransactionResult(GoldTransactionResultType transactionResult, Float amountNotProcessed) {


        result = transactionResult;
        notProcessed = amountNotProcessed;
    }

    public GoldTransactionResultType getResult() {

        return result;
    }

    public Float getNotProcessed() {

        return notProcessed;
    }
}

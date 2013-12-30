package me.igwb.GoldenChest.Vault;

import java.util.List;

import me.igwb.GoldenChest.GoldenChestEconomy;
import me.igwb.GoldenChest.TransactionResult;
import me.igwb.GoldenChest.Database.DBAddResult;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultConnector implements Economy {

    private GoldenChestEconomy parentPlugin;

    public VaultConnector(GoldenChestEconomy parent) {

        parentPlugin = parent;
    }


    @Override
    public EconomyResponse bankBalance(String arg0) {

        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "GoldenChest economy does not support banks.");
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {

        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "GoldenChest economy does not support banks.");
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {

        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "GoldenChest economy does not support banks.");
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {

        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "GoldenChest economy does not support banks.");
    }

    @Override
    public EconomyResponse createBank(String arg0, String arg1) {

        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "GoldenChest economy does not support banks.");
    }

    @Override
    public boolean createPlayerAccount(String arg0) {

        return parentPlugin.getDbConnector().addPlayer(arg0) != DBAddResult.error;
    }

    @Override
    public boolean createPlayerAccount(String arg0, String arg1) {

        return parentPlugin.getDbConnector().addPlayer(arg0) != DBAddResult.error;
    }

    @Override
    public String currencyNamePlural() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String currencyNameSingular() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {

        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "GoldenChest economy does not support banks.");
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, double arg1) {

        parentPlugin.getTransactionManager().giveMoney(arg0, Float.parseFloat(String.valueOf(arg1)));

        return new EconomyResponse(arg1, getBalance(arg0), ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {

        parentPlugin.getTransactionManager().giveMoney(arg0, Float.parseFloat(String.valueOf(arg2)));

        return new EconomyResponse(arg2, getBalance(arg0), ResponseType.SUCCESS, null);
    }

    @Override
    public String format(double arg0) {

        return String.valueOf(arg0);
    }

    @Override
    public int fractionalDigits() {

        return -1;
    }

    @Override
    public double getBalance(String arg0) {

        return parentPlugin.getTransactionManager().getBalance(arg0);
    }

    @Override
    public double getBalance(String arg0, String arg1) {

        return parentPlugin.getTransactionManager().getBalance(arg0);
    }

    @Override
    public List<String> getBanks() {

        return null;
    }

    @Override
    public String getName() {

        return "GoldenChestEconomy";
    }

    @Override
    public boolean has(String arg0, double arg1) {

        return getBalance(arg0) >= arg1;
    }

    @Override
    public boolean has(String arg0, String arg1, double arg2) {

        return getBalance(arg0) >= arg2;
    }

    @Override
    public boolean hasAccount(String arg0) {

        return parentPlugin.getDbConnector().addPlayer(arg0) != DBAddResult.error;
    }

    @Override
    public boolean hasAccount(String arg0, String arg1) {

        return parentPlugin.getDbConnector().addPlayer(arg0) != DBAddResult.error;
    }

    @Override
    public boolean hasBankSupport() {

        return false;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, String arg1) {

        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "GoldenChest economy does not support banks.");
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, String arg1) {

        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "GoldenChest economy does not support banks.");
    }

    @Override
    public boolean isEnabled() {
        if (parentPlugin == null) {
            return false;
        } else {
            return parentPlugin.isEnabled();
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {

        switch (parentPlugin.getTransactionManager().takeMoney(arg0, Float.parseFloat(String.valueOf(arg1)))) {
        case successful:

            return new EconomyResponse(arg1, getBalance(arg0), ResponseType.SUCCESS, null);
        case insufficientFunds:

            return new EconomyResponse(arg1, getBalance(arg0), ResponseType.FAILURE, "insufficient funds");
        default:
            break;
        }

        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
      
        switch (parentPlugin.getTransactionManager().takeMoney(arg0, Float.parseFloat(String.valueOf(arg2)))) {
        case successful:

            return new EconomyResponse(arg2, getBalance(arg0), ResponseType.SUCCESS, null);
        case insufficientFunds:

            return new EconomyResponse(arg2, getBalance(arg0), ResponseType.FAILURE, "insufficient funds");
        default:
            break;
        }

        return null;
    }

}

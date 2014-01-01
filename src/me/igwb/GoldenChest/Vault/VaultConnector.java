package me.igwb.GoldenChest.Vault;

import me.igwb.GoldenChest.GoldenChestEconomy;
import me.igwb.GoldenChest.Database.DBAddResult;

public class VaultConnector {

    private GoldenChestEconomy parentPlugin;

    public VaultConnector(GoldenChestEconomy parent) {

        parentPlugin = parent;
    }

    public boolean createPlayerAccount(String arg0) {

        return parentPlugin.getDbConnector().addPlayer(arg0) != DBAddResult.error;
    }

    public boolean createPlayerAccount(String arg0, String arg1) {

        return parentPlugin.getDbConnector().addPlayer(arg0) != DBAddResult.error;
    }

    public String currencyNamePlural() {
        // TODO Auto-generated method stub
        return null;
    }

    public String currencyNameSingular() {
        // TODO Auto-generated method stub
        return null;
    }

    public void depositPlayer(String arg0, double arg1) {

        parentPlugin.getTransactionManager().giveMoney(arg0, Float.parseFloat(String.valueOf(arg1)));
    }

    public String format(double arg0) {

        return String.valueOf(arg0);
    }

    public int fractionalDigits() {

        return -1;
    }

    public double getBalance(String arg0) {

        return parentPlugin.getTransactionManager().getBalance(arg0);
    }

    public double getBalance(String arg0, String arg1) {

        return parentPlugin.getTransactionManager().getBalance(arg0);
    }

    public String getName() {

        return "GoldenChestEconomy";
    }

    public boolean has(String arg0, double arg1) {

        return getBalance(arg0) >= arg1;
    }

    public boolean has(String arg0, String arg1, double arg2) {

        return getBalance(arg0) >= arg2;
    }

    public boolean hasAccount(String arg0) {
        return parentPlugin.getDbConnector().addPlayer(arg0) != DBAddResult.error;
    }


    public boolean hasAccount(String arg0, String arg1) {

        return parentPlugin.getDbConnector().addPlayer(arg0) != DBAddResult.error;
    }

    public boolean isEnabled() {
        if (parentPlugin == null) {
            return false;
        } else {
            return parentPlugin.isEnabled();
        }
    }

    public void withdrawPlayer(String arg0, double arg1) {

        parentPlugin.getTransactionManager().takeMoney(arg0, Float.parseFloat(String.valueOf(arg1)));
    }

}

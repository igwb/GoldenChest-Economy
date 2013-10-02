package me.igwb.GoldenChest.Vault;

import java.util.List;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultConnector implements Economy {

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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createPlayerAccount(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String format(double arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int fractionalDigits() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getBalance(String arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getBalance(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<String> getBanks() {

        return null;
    }

    @Override
    public String getName() {

        return "GoldenChest-Economy";
    }

    @Override
    public boolean has(String arg0, double arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean has(String arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasAccount(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasAccount(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
        // TODO Auto-generated method stub
        return null;
    }

}

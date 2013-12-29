package me.igwb.GoldenChest;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyCommandExecutor implements CommandExecutor {


    private final Plugin parentPlugin;

    public MyCommandExecutor(Plugin parent) {

        parentPlugin = parent;

    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

        switch (arg1.getName().toLowerCase()) {
        case "registerchest":
            return registerChest(arg0);
        case "pay":

            break;
        case "money": case "balance": case "bal":
            if (arg3 == null || arg3.length < 1) {
                return balance(arg0, null);
            } else {
               return balance(arg0, arg3[0]);
            }
        case "grant":
            return grant(arg0, arg3);
        case "take":
            return take(arg0, arg3);
        default:
            break;
        }
        return false;
    }


    private boolean registerChest(CommandSender sender) {

        if (sender instanceof Player) {
            if (parentPlugin.getDbConnector().getPlayersChests(sender.getName()).size() < parentPlugin.getFileConfig().getInt("Chests.maximumAllowed")) {
                sender.sendMessage("Open a chest to register it.");
                parentPlugin.getChestRegisterer().startsRegistering((Player) sender);
            } else {
                sender.sendMessage("You are not allowed to register another chest.");
            }
        } else {
            sender.sendMessage("This command can only be run by a player!");
        }


        return true;
    }

    private boolean pay() {
        return false;
    }

    private boolean balance(CommandSender sender, String player) {

        float balance;

        if (player == null) {

            balance = parentPlugin.getTransactionManager().getBalance(sender.getName());
            sender.sendMessage("Your current balance is: " + balance + ".");
        } else {

            balance = parentPlugin.getTransactionManager().getBalance(player);
            sender.sendMessage(player + " currently has " + balance + ".");
        }



        return true;
    }

    private boolean grant(CommandSender sender, String[] arg3) {
        try {
            if (arg3 != null && arg3.length == 2) {

                parentPlugin.getTransactionManager().storeOverflowToChest(arg3[0]);

                parentPlugin.getTransactionManager().giveMoney(arg3[0], Float.parseFloat(arg3[1]));
                sender.sendMessage(arg3[0] + " was granted " + arg3[1] + ".");
                sender.sendMessage(parentPlugin.getGoldConverter().convertMoneyToGold(Float.parseFloat(arg3[1])).toString());
            } else {
                sender.sendMessage("This command accepts two arguments only!");
                sender.sendMessage("/grant [player] [amount]");
            }

            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("Amount must be a number!");
            sender.sendMessage("/grant [player] [amount]");
            return true;
        } catch (Exception e) {
            sender.sendMessage("Invalid argument!");
            sender.sendMessage("/grant [player] [amount]");
            e.printStackTrace();
            return true;
        }
    }

    private boolean take(CommandSender sender, String[] arg3) {
        try {
            TransactionResult result;

            if (arg3 != null && arg3.length == 2) {

                parentPlugin.getTransactionManager().storeOverflowToChest(arg3[0]);

                result = parentPlugin.getTransactionManager().takeMoney(arg3[0], Float.parseFloat(arg3[1]));

                switch (result) {
                case successful:
                    sender.sendMessage(arg3[0] + " lost " + arg3[1] + ".");
                    sender.sendMessage(parentPlugin.getGoldConverter().convertMoneyToGold(Float.parseFloat(arg3[1])).toString());
                    break;
                case insufficientFunds:
                    sender.sendMessage(arg3[0] + " doesn't have enough money.");
                    break;
                default:
                    break;
                }

            } else {
                sender.sendMessage("This command accepts two arguments only!");
                sender.sendMessage("/take [player] [amount]");
            }

            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("Amount must be a number!");
            sender.sendMessage("/take [player] [amount]");
            return true;
        } catch (Exception e) {
            sender.sendMessage("Invalid argument!");
            sender.sendMessage("/take [player] [amount]");
            e.printStackTrace();
            return true;
        }
    }
}

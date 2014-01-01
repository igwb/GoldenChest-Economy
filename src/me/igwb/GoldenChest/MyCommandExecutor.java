package me.igwb.GoldenChest;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyCommandExecutor implements CommandExecutor {


    private final GoldenChestEconomy parentPlugin;

    public MyCommandExecutor(GoldenChestEconomy parent) {

        parentPlugin = parent;

    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

        switch (arg1.getName().toLowerCase()) {
        case "registerchest":
            if (arg0.hasPermission("GoldenChest.registerChest")) {
                return registerChest(arg0);
            } else {
                arg0.sendMessage("Insufficient permissions!");
                return true;
            }
        case "pay":
            if (arg0.hasPermission("GoldenChest.pay")) {
                return pay(arg0, arg3);
            } else {
                arg0.sendMessage("Insufficient permissions!");
                return true;
            }
        case "money": case "balance": case "bal":
            if (arg0.hasPermission("GoldenChest.money")) {
                if (arg3 == null || arg3.length < 1) {
                    return balance(arg0, null);
                } else {
                    return balance(arg0, arg3[0]);
                }
            } else {
                arg0.sendMessage("Insufficient permissions!");
                return true;
            }
        case "grant":
            if (arg0.hasPermission("GoldenChest.grant")) {
                return grant(arg0, arg3);
            } else {
                arg0.sendMessage("Insufficient permissions!");
                return true;
            }
        case "take":
            if (arg0.hasPermission("GoldenChest.take")) {
                return take(arg0, arg3);
            } else {
                arg0.sendMessage("Insufficient permissions!");
                return true;
            }
        case "top":
            if (arg0.hasPermission("GoldenChest.top")) {
                return top(arg0, arg3);
            } else {
                arg0.sendMessage("Insufficient permissions!");
                return true;
            }
        default:
            break;
        }
        return false;
    }

    private boolean top(CommandSender sender, String[] args) {
        try {
            int page;
            int entriesPerPage = 5;

            ArrayList<String> topList;

            if (args != null && args.length <= 1) {

                if (args.length == 1) {
                    page = Integer.parseInt(args[0]);
                } else {
                    page = 1;
                }
                page = Math.max(0, page - 1);

                topList = parentPlugin.getDbConnector().getTopList();

                if (topList != null) {
                    for (int i = page * entriesPerPage; i < Math.min(topList.size(), (page * entriesPerPage) + entriesPerPage); i++) {
                        sender.sendMessage(topList.get(i));
                    }
                }

            } else {
                sender.sendMessage("This command accepts a maximum of one argument!");
                sender.sendMessage("/top [page]");
            }
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("Page must be a number!");
            sender.sendMessage("/top [page]");
            return true;
        } catch (Exception e) {
            sender.sendMessage("Invalid argument!");
            sender.sendMessage("/top [page]");
            e.printStackTrace();
            return true;
        }
    }

    private boolean registerChest(CommandSender sender) {

        if (sender instanceof Player) {

            //Check if the player is in a world where he can register a chest.
            if (parentPlugin.getFileConfig().getList("Worlds").contains(((Player) sender).getLocation().getWorld().getName())) {

                //Check if the player is allowed to register more chests.
                if (parentPlugin.getDbConnector().getPlayersChests(sender.getName()).size() < parentPlugin.getFileConfig().getInt("Chests.maximumAllowed")) {
                    sender.sendMessage("Open a chest to register it.");
                    parentPlugin.getChestRegisterer().startsRegistering((Player) sender);
                } else {
                    sender.sendMessage("You are not allowed to register another chest.");
                }
            } else {
                sender.sendMessage("Chests can not be registered in this world!");
            }
        } else {
            sender.sendMessage("This command can only be run by a player!");
        }

        return true;
    }

    private boolean pay(CommandSender sender, String[] args) {

        try {
            //Check if payments are enabled
            if (parentPlugin.getFileConfig().getBoolean("Payments.enablePayCommand")) {

                //check if we are dealing with a player
                if (sender instanceof Player) {

                    //Check if the argument count is correct
                    if (args != null && args.length == 2) {

                        //Check if the player has an account, ignore if disabled in the config.
                        if (!parentPlugin.getFileConfig().getBoolean("Payments.canUseWithoutWallet") && parentPlugin.getDbConnector().getPlayersChests(args[0]).size() > 0) {

                            //Check if the player has enough money.
                            if (parentPlugin.getTransactionManager().getBalance(((Player) sender).getName()) >= Float.parseFloat(args[1])) {

                                //Finally pay the money.
                                if (parentPlugin.getTransactionManager().takeMoney(((Player) sender).getName(), Float.parseFloat(args[1])) == TransactionResult.successful) {
                                    parentPlugin.getTransactionManager().giveMoney(args[0], Float.parseFloat(args[1]));
                                    sender.sendMessage("You've payed " + args[1] + " to " + args[0]);
                                }
                            } else {
                                sender.sendMessage("You do not have enough money!");
                            }
                        } else {
                            sender.sendMessage("The player you specified does not have an account.");
                        }
                    } else {
                        sender.sendMessage("This command accepts two arguments only!");
                        sender.sendMessage("/pay [player] [amount]");
                    }
                } else {
                    sender.sendMessage("This command can only be run by a player!");
                }
            } else {
                sender.sendMessage("/pay is currently disabled.");
            }

            return true;

        } catch (NumberFormatException e) {
            sender.sendMessage("Amount must be a number!");
            sender.sendMessage("/pay [player] [amount]");
            return true;
        } catch (Exception e) {
            sender.sendMessage("Invalid argument!");
            sender.sendMessage("/pay [player] [amount]");
            e.printStackTrace();
            return true;
        }
    }

    private boolean balance(CommandSender sender, String player) {

        float balance;

        if (player == null) {

            //Check if the player has any chests registered
            if (parentPlugin.getDbConnector().getPlayersChests(sender.getName()).size() > 0) {
                balance = parentPlugin.getTransactionManager().getBalance(sender.getName());
                sender.sendMessage("Your current balance is: " + balance + ".");
            } else {
                sender.sendMessage("You do not have any chests registered!");
                sender.sendMessage("To register a chest type /registerChest and open it.");
            }
        } else {
            if (parentPlugin.getDbConnector().getPlayersChests(player).size() > 0) {
                balance = parentPlugin.getTransactionManager().getBalance(player);
                sender.sendMessage(player + " currently has " + balance + ".");
            } else {
                sender.sendMessage(player + " does not have any chests registered!");
            }
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

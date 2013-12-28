package me.igwb.GoldenChest;

import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
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
            registerChest(arg0);

            break;
        case "pay":

            break;
        case "money": case "balance": case "bal":
            if (arg3 == null || arg3.length < 1) {
                balance(arg0, null);
            } else {
                balance(arg0, arg3[0]);
            }
            break;
        default:

            break;
        }
        return false;
    }


    private boolean registerChest(CommandSender sender) {

        Player player;

        if (sender instanceof Player) {
            parentPlugin.getChestRegisterer().startsRegistering((Player) sender);
        } else {
            sender.sendMessage("This command can only be run by a player!");
        }


        return false;
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
}

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

}

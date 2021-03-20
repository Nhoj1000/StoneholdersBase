package io.github.nhoj1000.stoneholdersbase.commands;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Command to set the target's stone
 * Usage: /stone [add, remove, clear] [target] [stone]
 */
public class StoneSetCommand implements TabExecutor {
    StoneholdersBase plugin;

    public StoneSetCommand(StoneholdersBase plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player user = (Player) sender;
            Player target;

            target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                user.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }

            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("clear")) {
                    plugin.getStoneholderMap().remove(target);
                    target.sendMessage("No longer a stoneholder.");
                }
            } else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("add")) {
                    Stone stone = plugin.getStone(args[2]);
                    if(stone != null) {
                        Stoneholder temp = new Stoneholder(target); //TODO fix it for generic stones and cover extra cases
                        temp.addStone(stone);
                        plugin.getStoneholderMap().put(target, temp);
                    }
                } else if(args[0].equalsIgnoreCase("remove")) {

                } else
                    return false;
            } else
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}

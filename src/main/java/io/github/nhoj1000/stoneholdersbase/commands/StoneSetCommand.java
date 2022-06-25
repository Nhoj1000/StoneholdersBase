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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Command to set the target's stone
 * Usage: /stone [add, remove, clear] [target] [stone]
 */
public class StoneSetCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 2) {return false;}

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Target not found!");
        } else {
            Stoneholder stoneholder = StoneholdersBase.getStoneholder(target);
            return switch (args[0]) {
                case "clear" -> clearStones(sender, stoneholder, args);
                case "add" -> addStone(sender, stoneholder, args);
                case "remove" -> removeStone(sender, stoneholder, args);
                case "list" -> listStones(sender, stoneholder, args);
                default -> false;
            };
        }

        return true;
    }

    public boolean clearStones(CommandSender sender, Stoneholder stoneholder, String[] args) {
        if(stoneholder.hasStones()) {
            stoneholder.clearStones();
        } else {
            sender.sendMessage(ChatColor.RED + args[1] + " is not a stoneholder!");
        }
        return true;
    }

    public boolean addStone(CommandSender sender, Stoneholder stoneholder, String[] args) {
        if(args.length < 3) {return false;}

        Stone stone = StoneholdersBase.getStoneFromName(args[2]);
        if(stone == null) {
            sender.sendMessage(args[2] + " is not a valid stone.");
            return true;
        } else if (!stoneholder.hasStones()) {
            sender.sendMessage(args[1] + " is now a stoneholder.");
        }
        sender.sendMessage(String.format("%s %s %s",
                args[1],
                stoneholder.addStone(stone) ? "has acquired the" : "already has the",
                stone
                ));
        return true;
    }

    public boolean removeStone(CommandSender sender, Stoneholder stoneholder, String[] args) {
        if(args.length < 3) {return false;}

        Stone stone = StoneholdersBase.getStoneFromName(args[2]);
        if(stone == null) {
            sender.sendMessage(args[2] + " is not a valid stone.");
            return true;
        }
            sender.sendMessage(String.format("%s %s %s",
                args[1],
                stoneholder.removeStone(stone) ? "no longer has the" : "does not have the",
                stone
        ));
        if (!stoneholder.hasStones()) {
            sender.sendMessage(args[1] + " is no longer a stoneholder.");
        }
        return true;
    }

    public boolean listStones(CommandSender sender, Stoneholder stoneholder, String[] args) {
        if(stoneholder.hasStones()) {
            StringBuilder key = new StringBuilder(args[1] + "'s stones:\n");
            stoneholder.getStones().forEach(s -> key.append(ChatColor.WHITE).append("> ").append(s).append("\n"));
            sender.sendMessage(key.toString());
        } else {
            sender.sendMessage(ChatColor.RED + args[1] + " is not a stoneholder!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return switch (args.length) {
            case 1 -> trimList(Arrays.asList("add", "remove", "clear", "list"), args[0]);
            case 3 -> trimList(new ArrayList<>(StoneholdersBase.getStoneNameMap().keySet()), args[2]);
            default -> null;
        };
    }

    private List<String> trimList(List<String> list, String start) {
        return list.stream().filter(s -> s.startsWith(start)).collect(Collectors.toList());
    }
}

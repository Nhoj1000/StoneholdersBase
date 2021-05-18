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

/**
 * Command to set the target's stone
 * Usage: /stone [add, remove, clear] [target] [stone]
 */
public class StoneSetCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;

        if(args.length > 1) {   //Ensures 2 args entered
            target = Bukkit.getPlayer(args[1]);
            if (target == null) //Finds target player
                return informAndReturn(sender, ChatColor.RED + "Target not found!", true);
            Stoneholder s = StoneholdersBase.getStoneholder(target);

            if (args.length == 2) { //clear and list commands
                if (!s.isStoneholder())
                    return informAndReturn(sender,ChatColor.RED + "Target is not a stoneholder!", true);

                if (args[0].equals("clear")) {
                    s.clearStones();
                    return informAndReturn(sender, "Target's stones have been cleared.", true);
                } else if (args[0].equals("list")) {
                    StringBuilder key = new StringBuilder(target.getName() + "'s stones:\n");
                    for(Stone tempStone: s.getStones())
                        key.append(ChatColor.WHITE).append("> ").append(tempStone).append("\n");
                    return informAndReturn(sender, key.toString(), true);
                }
            } else if (args.length == 3) {  //add and remove commands
                Stone stone = StoneholdersBase.getStoneFromName(args[2]);
                if (stone == null)
                    return informAndReturn(sender, ChatColor.RED + "Stone not found!", true);

                if (args[0].equals("add")) {
                    if (!s.isStoneholder()) sender.sendMessage(target.getName() + " is now a stoneholder.");
                    if (s.addStone(stone)) sender.sendMessage(target.getName() + " has acquired the " + stone);
                    else sender.sendMessage(target.getName() + " already has the " + stone);
                    return true;
                } else if (args[0].equals("remove")) {
                    if(s.removeStone(stone)) sender.sendMessage(target.getName() + " no longer has the " + stone);
                    else sender.sendMessage(target.getName() + " does not have the " + stone);
                    if (!s.isStoneholder()) sender.sendMessage(target.getName() + " is no longer a stoneholder.");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                return trimList(Arrays.asList("add", "remove", "clear", "list"), args[0]);
            case 3:
                return trimList(new ArrayList<>(StoneholdersBase.getStoneNameMap().keySet()), args[2]);
            default:
                return null;
        }
    }

    private List<String> trimList(List<String> list, String start) {
        List<String> listCopy = new ArrayList<>(list);
        for(int i = 0; i < listCopy.size(); i++)
            if(!listCopy.get(i).startsWith(start)) {
                listCopy.remove(i);
                i--;
            }
        return listCopy;
    }

    private boolean informAndReturn(CommandSender sender, String message, boolean returnValue) {
        sender.sendMessage(message);
        return returnValue;
    }
}

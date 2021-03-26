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
    private final StoneholdersBase plugin;

    public StoneSetCommand() {
        plugin = StoneholdersBase.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player user = (Player) sender;
            Player target = null;
            Stoneholder temp;

            if(args.length > 1) {
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    user.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }
            }

            Map<UUID, Stoneholder> map = plugin.getStoneholderMap();

            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("clear")) {
                    map.remove(target.getUniqueId()).clearStones();
                    target.sendMessage("No longer a stoneholder."); //TODO Clean up this mess lmao
                }
            } else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("add")) {
                    Stone stone = plugin.getStone(args[2]);
                    if (stone != null) {
                        temp = map.get(target.getUniqueId());
                        if(temp == null)
                            temp = new Stoneholder(target);
                        temp.addStone(stone);
                        map.putIfAbsent(target.getUniqueId(), temp);
                    }
                } else if(args[0].equalsIgnoreCase("remove")) {
                    Stone stone = plugin.getStone(args[2]);
                    if (stone != null) {
                        temp = map.get(target.getUniqueId());
                        if(temp != null) {
                            temp.removeStone(stone);
                            if(!temp.isStoneholder())
                                map.remove(target.getUniqueId());
                        }
                    }
                } else
                    return false;
            } else
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                return trimList(Arrays.asList("add", "remove", "clear"), args[0]);
            case 3:
                return trimList(new ArrayList<>(plugin.getStones().keySet()), args[2]);
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
}

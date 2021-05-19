package io.github.nhoj1000.stoneholdersbase.commands;

import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class NoMana implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Stoneholder s;

        if(args.length == 0 && sender instanceof Player)
            s = StoneholdersBase.getStoneholder((Player) sender);
        else {
            Player otherPlayer = Bukkit.getPlayer(args[0]);
            if(otherPlayer != null)
                s = StoneholdersBase.getStoneholder(otherPlayer);
            else {
                sender.sendMessage(ChatColor.RED + "Player not found");
                return true;
            }
        }

        if(s.isStoneholder()) {
            if (s.toggleManaRequired())
                s.getPlayer().sendMessage("Mana now required!");
            else
                s.getPlayer().sendMessage("Mana no longer required!");
        } else
            sender.sendMessage(ChatColor.RED + "User is not a stoneholder!");

        return true;
    }
}

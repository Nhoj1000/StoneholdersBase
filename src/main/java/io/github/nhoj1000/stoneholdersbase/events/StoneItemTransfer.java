package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.reality.GenerateArrow;
import io.github.nhoj1000.stoneholdersbase.powers.reality.GlassBow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

//Handles the transfer of stones and stone items
public class StoneItemTransfer implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        Stoneholder sh = StoneholdersBase.getStoneholder(p);

        ItemStack item = e.getItemDrop().getItemStack();
        Stone stone = StoneholdersBase.getStoneFromItem(item);

        if(stone != null)
            sh.removeStone(stone);
        else {
            for(Stone s: sh.getStones())
                for(ItemStack i: s.getPlayerItems())
                    if(StoneholdersBase.comparePowerItems(i, item))
                        e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        Stoneholder sh = StoneholdersBase.getStoneholder(p);
        ItemStack item = e.getItem().getItemStack();
        Stone s = StoneholdersBase.getStoneFromItem(item);

        if(s != null && !sh.getStones().contains(s)) {
            sh.addStone(s);
            e.getItem().remove();
            e.setCancelled(true);
        }

        if(StoneholdersBase.comparePowerItems(item, GlassBow.getGlassArrow())) {
            ItemStack arrows = null;
            ItemStack comparison = GlassBow.getGlassArrow();
            for(ItemStack temp: p.getInventory().getContents())
                if(StoneholdersBase.comparePowerItems(temp, comparison)) arrows = item;
            if (arrows == null || arrows.getAmount() < GenerateArrow.getMaxArrows())
                p.getInventory().addItem(GlassBow.getGlassArrow());
            p.sendMessage(ChatColor.RED + "Maximum arrow count reached!");
        }
    }
}

package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
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
        if(sh == null) return;

        ItemStack item = e.getItemDrop().getItemStack();
        Stone stone = StoneholdersBase.getStoneFromItem(item);

        if(stone != null)
            sh.removeStone(stone);
        else {
            for(Stone s: sh.getStones())
                if(s.getPlayerItems().contains(item))
                    e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
    }
}

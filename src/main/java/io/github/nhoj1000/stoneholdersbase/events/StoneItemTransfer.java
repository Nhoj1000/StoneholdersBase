package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneUtils;
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

import static io.github.nhoj1000.stoneholdersbase.StoneConstants.REALITY_ID;

//Handles the transfer of stones and stone items
public class StoneItemTransfer implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Stoneholder sh = StoneholdersBase.getStoneholder(e.getPlayer());

        ItemStack item = e.getItemDrop().getItemStack();
        Stone stone = StoneUtils.getStoneFromItem(item);

        if(stone != null) {
            sh.removeStone(stone);
        } else if(sh.getStones().stream()
                    .flatMap(s -> s.getPlayerItems().stream())
                    .anyMatch(i -> StoneUtils.comparePowerItems(i, item))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) {return;}
        Player p = (Player) e.getEntity();
        Stoneholder stoneholder = StoneholdersBase.getStoneholder(p);
        ItemStack item = e.getItem().getItemStack();
        Stone stone = StoneUtils.getStoneFromItem(item);

        if(stone != null) {
            if (!stoneholder.getStones().contains(stone)) {
                stoneholder.addStone(stone);
                e.getItem().remove();
            }
            e.setCancelled(true);
        }

        if(stoneholder.hasStone(REALITY_ID) && StoneUtils.comparePowerItems(item, GlassBow.getGlassArrow())) {
            new GenerateArrow(5).usePower(p);
            e.getItem().remove();
            e.setCancelled(true);
        }
    }
}

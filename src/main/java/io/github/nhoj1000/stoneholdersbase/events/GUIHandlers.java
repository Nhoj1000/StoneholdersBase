package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIHandlers implements Listener {

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Power Selection")) {
            Player player = (Player) e.getWhoClicked();

            if(e.getCurrentItem() == null) return;
            ItemStack item = e.getCurrentItem();
            if(item.getType() == Material.BARRIER) player.closeInventory();

            for(Stone s: StoneholdersBase.getStoneNameMap().values())
                for(Power p: s.getPowerSet())
                    if(p.getTool().equals(item)) {
                        StoneholdersBase.getStoneholder(player).setStonePower(p);
                        break;
                    }
        }
    }
}

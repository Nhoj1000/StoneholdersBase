package io.github.nhoj1000.stoneholdersbase.events;

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
            ItemStack item = e.getCurrentItem();

            if(item != null) {
                if (item.getType() == Material.BARRIER) {
                    player.closeInventory();
                    e.setCancelled(true);
                }

                Power power = StoneholdersBase.getStoneNameMap().values().stream()
                        .flatMap(s -> s.getPowerSet().stream())
                        .filter(p -> StoneholdersBase.comparePowerItems(p.getTool(), item))
                        .findFirst().orElse(null);

                if (power != null) {
                    StoneholdersBase.getStoneholder(player).setStonePower(power);
                    player.closeInventory();
                    e.setCancelled(true);
                }
            }
        }
    }
}

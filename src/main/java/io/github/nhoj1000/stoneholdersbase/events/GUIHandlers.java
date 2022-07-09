package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static io.github.nhoj1000.stoneholdersbase.StoneConstants.SELECT_POWER_GUI_TITLE;

public class GUIHandlers implements Listener {

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals(SELECT_POWER_GUI_TITLE)) {
            Player player = (Player) e.getWhoClicked();
            ItemStack item = e.getCurrentItem();

            if(item != null) {
                if (item.getType() == Material.BARRIER) {
                    player.closeInventory();
                }

                Power power = StoneUtils.getAllStones().stream()
                        .flatMap(s -> s.getPowerSet().stream())
                        .filter(p -> StoneUtils.comparePowerItems(p.getTool(), item))
                        .findFirst().orElse(null);

                if (power != null) {
                    StoneholdersBase.getStoneholder(player).setStonePower(power);
                    player.closeInventory();
                }
            }
            e.setCancelled(true);
        }
    }
}

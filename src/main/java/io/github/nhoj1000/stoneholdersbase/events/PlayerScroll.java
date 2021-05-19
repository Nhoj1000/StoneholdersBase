package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerScroll implements Listener {
    @EventHandler
    public void onScroll(PlayerItemHeldEvent e) {
        Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(), () ->
                StoneholdersBase.getStoneholder(e.getPlayer()).manaPreview(e.getPlayer().getInventory().getItemInMainHand()), 0L);
    }
}

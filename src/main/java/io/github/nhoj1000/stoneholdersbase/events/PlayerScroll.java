package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerScroll implements Listener {
    @EventHandler
    public void onScroll(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        StoneholdersBase.getStoneholder(p).manaPreview(p.getInventory().getItem(e.getNewSlot()));
    }
}

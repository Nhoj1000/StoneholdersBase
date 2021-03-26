package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerActivatePower implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Stoneholder p = StoneholdersBase.getStoneholderMap().get(e.getPlayer().getUniqueId());
        if(p != null)
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                if(p.useStonePower(e.getItem()))
                    e.setCancelled(true);
    }

    @EventHandler
    public void onLeftClick(PlayerAnimationEvent e) {
        Player p = e.getPlayer();
        Stoneholder s = StoneholdersBase.getStoneholderMap().get(p.getUniqueId());
        if(s != null)
            s.useSpecialPower(p.getInventory().getItemInMainHand());
    }
}

package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.powers.time.Pause;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EntityMovingEvent implements Listener {
    @EventHandler
    public void onEntityMove(PlayerMoveEvent e) {
        if(Pause.isFrozen(e.getPlayer()))
            e.setCancelled(true);
    }
}

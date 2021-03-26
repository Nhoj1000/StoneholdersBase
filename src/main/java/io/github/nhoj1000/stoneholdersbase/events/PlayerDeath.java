package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.powers.soul.SoulCollector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        int collectionRadius = SoulCollector.getCollectionRadius();

        for(Entity entity: player.getNearbyEntities(collectionRadius, collectionRadius, collectionRadius)) {
            if(entity instanceof Player)
                SoulCollector.addSoul((Player) entity, player);
        }
    }
}

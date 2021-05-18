package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.UniquePower;
import io.github.nhoj1000.stoneholdersbase.powers.soul.SoulCollector;
import io.github.nhoj1000.stoneholdersbase.powers.time.Checkpoint;
import io.github.nhoj1000.stoneholdersbase.powers.time.Pause;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerDeath implements Listener {
    private static final int collectionRadius = SoulCollector.getCollectionRadius();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        if(Checkpoint.isImmortal(player)) {
            e.setKeepInventory(true);
            e.setKeepLevel(true);
            e.getDrops().clear();
            Location loc = player.getLocation();
            Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(), () -> {
                player.spigot().respawn();
                player.teleport(loc);
                player.setGameMode(GameMode.SPECTATOR);
            }, 1L);
            Pause.setFrozen(player, true);
        } else {
            //soul collection code
            for(Entity entity: player.getNearbyEntities(collectionRadius, collectionRadius, collectionRadius))
                if(entity instanceof Player)
                    SoulCollector.addSoul((Player) entity, player);

            //filters out item drops for stone power items
            List<ItemStack> drops = e.getDrops();
            for(Stone stone: StoneholdersBase.getStoneholder(player).getStones())
                for(UniquePower up: stone.getUniquePowerMap().values())
                    drops.removeAll(up.getItems());
        }
    }
}

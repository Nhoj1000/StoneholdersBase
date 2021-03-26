package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.reality.GlassBow;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamagedByEntity implements Listener{
    private StoneholdersBase plugin;

    public PlayerDamagedByEntity() {
        plugin = StoneholdersBase.getInstance();
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            Stoneholder p = plugin.getStoneholderMap().get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            if(p != null)
                p.useSpecialPower(p.getPlayer().getInventory().getItemInMainHand());
        }

        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Stoneholder stoneholder = plugin.getStoneholderMap().get(p.getUniqueId());
            if(stoneholder != null)
                if(p.isBlocking() && p.isSneaking())
                    if(p.getInventory().getItemInMainHand().getType() == Material.SHIELD)
                        stoneholder.useSpecialPower(p.getInventory().getItemInMainHand());
                    else
                        stoneholder.useSpecialPower(p.getInventory().getItemInOffHand());
        }
    }
}

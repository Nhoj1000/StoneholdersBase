package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.reality.GlassBow;
import io.github.nhoj1000.stoneholdersbase.powers.time.Pause;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamagedByEntity implements Listener{
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(Pause.isFrozen(e.getEntity()))
            e.setCancelled(true);

        if(e.getDamager() instanceof Arrow && e.getEntity() instanceof LivingEntity &&
                ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            Player shooter = (Player) ((Arrow) e.getDamager()).getShooter();
            Stoneholder p = StoneholdersBase.getStoneholder(shooter);
            if (p != null)
                if (((LivingEntity) e.getEntity()).getHealth() - e.getDamage() > 0)
                    GlassBow.setTarget(shooter, e.getEntity(), true);
                else
                    GlassBow.clearTarget(shooter, false);
        }

        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(!StoneholdersBase.isStoneholder(p)) return;

            Stoneholder s = StoneholdersBase.getStoneholder(p);
            if(p.isBlocking() && p.isSneaking())
                if(p.getInventory().getItemInMainHand().getType() == Material.SHIELD)
                    s.useUniquePower(p.getInventory().getItemInMainHand());
                else
                    s.useUniquePower(p.getInventory().getItemInOffHand());
        }
    }
}

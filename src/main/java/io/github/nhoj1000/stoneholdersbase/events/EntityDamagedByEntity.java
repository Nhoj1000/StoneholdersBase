package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.reality.GlassBow;
import io.github.nhoj1000.stoneholdersbase.powers.time.Pause;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.PotionMeta;

import static io.github.nhoj1000.stoneholdersbase.StoneConstants.*;

public class EntityDamagedByEntity implements Listener{
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(Pause.isFrozen(e.getEntity())) {
            e.setCancelled(true);
        }

        if(e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getCustomEffects().equals(((PotionMeta) GlassBow.getGlassArrow().getItemMeta()).getCustomEffects())) {
                Player shooter = (Player) arrow.getShooter();
                Stoneholder sh = StoneholdersBase.getStoneholder(shooter);
                if (sh.hasStone(REALITY_ID)) {
                    if (isEntityDead(e.getEntity(), e.getDamage())) {
                        GlassBow.clearTarget(shooter, false);
                    } else {
                        GlassBow.setTarget(shooter, e.getEntity(), true);
                    }
                }
            }
        }

        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Stoneholder stoneholder = StoneholdersBase.getStoneholder(p);

            if((stoneholder.hasStone(POWER_ID) || stoneholder.hasStone(TIME_ID)) && p.isBlocking() && p.isSneaking()) {
                stoneholder.useUniquePower(p.getInventory().getItemInMainHand().getType() == Material.SHIELD
                        ? p.getInventory().getItemInMainHand()
                        : p.getInventory().getItemInOffHand());
            }
        }
    }

    private boolean isEntityDead(Entity e, double damage) {
        return (e instanceof LivingEntity) && (((LivingEntity) e).getHealth() - damage > 0);
    }
}

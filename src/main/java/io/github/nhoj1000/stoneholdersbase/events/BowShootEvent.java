package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.reality.GlassBow;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class BowShootEvent implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(StoneholdersBase.getStoneholder(p).hasStones() && StoneUtils.comparePowerItems(e.getConsumable(), GlassBow.getGlassArrow())) {
                GlassBow.setTarget(p, e.getProjectile(), false);
                ((Arrow) e.getProjectile()).setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            }
        }
    }
}

package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.reality.GlassBow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerActivatePower implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Stoneholder s = StoneholdersBase.getStoneholder(e.getPlayer());
        if(s.isStoneholder())
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                s.useNormalPower(StoneholdersBase.getStoneFromItem(e.getItem()));
    }

    //Handles power swapping for a given stone
    //Also used for special items with left click function like the glass bow
    @EventHandler
    public void onLeftClick(PlayerAnimationEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        Stone s = StoneholdersBase.getStoneFromItem(item);
        Stoneholder sh = StoneholdersBase.getStoneholder(p);
        if(s != null)
            sh.selectPowerGUI(s);
        else if(sh.isStoneholder())
            if(item.equals(GlassBow.getGlassBow()))
                sh.useUniquePower(item);
    }
}

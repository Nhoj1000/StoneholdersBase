package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.Stoneholder;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.reality.GlassBow;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerActivatePower implements Listener {
    private static final Set<Material> ignoredBlocks = setIgnoredBlocks();

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getItem() != null && p.getCooldown(e.getItem().getType()) > 0) return;
        //Special blocks that shouldn't trigger powers
        if(e.getClickedBlock() != null && ignoredBlocks.contains(e.getClickedBlock().getType())) return;

        Stoneholder sh = StoneholdersBase.getStoneholder(p);
        if(sh.isStoneholder())
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                sh.useNormalPower(StoneholdersBase.getStoneFromItem(e.getItem()));
            else {
                //Handles power swapping for a given stone
                //Also used for special items with left click function like the glass bow
                ItemStack item = p.getInventory().getItemInMainHand();
                Stone s = StoneholdersBase.getStoneFromItem(item);

                if(s != null)
                    sh.selectPowerGUI(s);
                else if(sh.isStoneholder())
                    if(item.equals(GlassBow.getGlassBow()))
                        sh.useUniquePower(item);
            }
    }

    /**
     * Utility method to load in the set of blocks that shouldn't trigger a power upon right click
     */
    public static Set<Material> setIgnoredBlocks() {
        Set<Material> ignored = new HashSet<>();
        ignored.addAll(Tag.TRAPDOORS.getValues());
        ignored.addAll(Tag.DOORS.getValues());
        ignored.addAll(Tag.BUTTONS.getValues());
        ignored.addAll(Tag.FENCE_GATES.getValues());
        ignored.addAll(Tag.BEDS.getValues());
        ignored.addAll(Arrays.asList(Material.REPEATER, Material.COMPARATOR, Material.LEVER, Material.DAYLIGHT_DETECTOR, Material.BELL));

        return ignored;
    }
}

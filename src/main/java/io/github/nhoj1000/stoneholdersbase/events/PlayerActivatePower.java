package io.github.nhoj1000.stoneholdersbase.events;

import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneUtils;
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

import static io.github.nhoj1000.stoneholdersbase.StoneConstants.REALITY_ID;

public class PlayerActivatePower implements Listener {
    private static final Set<Material> ignoredBlocks = getIgnoredBlocks();

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if((item != null && p.getCooldown(e.getItem().getType()) > 0)
                || (e.getClickedBlock() != null && ignoredBlocks.contains(e.getClickedBlock().getType()))) {
            return;
        }

        Stoneholder sh = StoneholdersBase.getStoneholder(p);
        if(sh.hasStones()) {
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                sh.useNormalPower(StoneUtils.getStoneFromItem(e.getItem()));
                if (sh.hasStone(REALITY_ID) && GlassBow.getGlassBow().equals(item)) {
                    sh.useUniquePower(item);
                }
            } else {//Handles power swapping for a given stone
                Stone s = StoneUtils.getStoneFromItem(item);

                if (s != null) {
                    sh.selectPowerGUI(s);
                }
            }
        }
    }

    /**
     * Utility method to load in the set of blocks that shouldn't trigger a power upon right click
     */
    public static Set<Material> getIgnoredBlocks() {
        return new HashSet<>(){{
            addAll(Tag.TRAPDOORS.getValues());
            addAll(Tag.DOORS.getValues());
            addAll(Tag.BUTTONS.getValues());
            addAll(Tag.FENCE_GATES.getValues());
            addAll(Tag.BEDS.getValues());

            add(Material.REPEATER);
            add(Material.COMPARATOR);
            add(Material.LEVER);
            add(Material.DAYLIGHT_DETECTOR);
            add(Material.BELL);
        }};
    }
}

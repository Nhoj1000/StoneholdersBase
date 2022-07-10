package io.github.nhoj1000.stoneholdersbase.powers.space;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class Dash implements Power {
    private final int dashDistance;

    public Dash(int dashDistance) {
        this.dashDistance = dashDistance;
    }

    @Override
    public boolean usePower(Player player) {
        List<Block> target = player.getLastTwoTargetBlocks(null, dashDistance);
        List<Entity> riders = player.getNearbyEntities(0.5, 1, 0.5);
        Vector velocity = player.getVelocity().clone();

        Location destination;

        if(isDashSafe(target.get(1).getLocation().add(0, 1, 0).getBlock())) {
            destination = target.get(1).getLocation().add(0.5, 1.0, 0.5);
        } else if (isDashSafe(target.get(0))){
            destination = target.get(0).getLocation().add(0.5, 0, 0.5);
        } else {
            player.sendMessage("Invalid dash location");
            return false;
        }

        destination.setDirection(player.getLocation().getDirection());
        player.teleport(StoneUtils.worldBorderBound(destination));
        player.setFallDistance(player.getFallDistance()/2);
        player.setVelocity(velocity);

        riders.forEach(rider -> rider.teleport(player));
        return true;
    }

    @Override
    public ItemStack getTool() {
        return StoneUtils.generateStoneTool(Material.GOLDEN_SHOVEL, 4, "Dash", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 35;
    }

    //return if a given block and it's headspace block aren't solid
    private boolean isDashSafe(Block dashTarget) {
        return !dashTarget.getLocation().add(0, 0, 0).getBlock().getType().isSolid()
                && !dashTarget.getLocation().add(0, 1, 0).getBlock().getType().isSolid();
    }
}
